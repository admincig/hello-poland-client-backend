package pl.hellopoland.order;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.lang.System.Logger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.ConflictingException;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.order.Order.Status;
import pl.hellopoland.sight.Portal;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.Ticket;
import pl.hellopoland.user.User;
import pl.hellopoland.user.UserService;
import pl.hellopoland.util.PaymentUtils;
import pl.hellopoland.util.Triplet;
import pl.hellopoland.util.Woo;

@LocalBean
@Stateless
public class OrderService extends ServiceSuperclass {

  @Inject
  UserService uService;

  @PermitAll
  public Order create(Collection<Triplet<Long, Date, Integer>> triplets, OrderDetails details) {
    User user = null;
    try {
      user = uService.me();
    } catch (NoResultException | EJBAccessException e) {
      // anonymous user
    }

    Order o = new Order();
    o.generateHash();
    o.setUser(user);
    o.setDetails(details);
    em.persist(o);

    Map<Long, List<Triplet<Long, Date, Integer>>> tripletsGroupedByTicketId =
        triplets.stream().collect(groupingBy(t -> t.first));
    Set<Long> ticketsIds = tripletsGroupedByTicketId.keySet();
    List<Ticket> tickets =
        em.createQuery("from Ticket t join fetch t.sight s where t.id in (:ids) order by s.id asc",
            Ticket.class).setParameter("ids", ticketsIds).getResultList();
    Map<Sight, List<Ticket>> ticketsGroupedBySight =
        tickets.stream().collect(groupingBy(Ticket::getSight));
    Map<Long, Ticket> ticketIdToObject = tickets.stream().collect(toMap(Ticket::getId, t -> t));
    for (Map.Entry<Sight, List<Ticket>> entry : ticketsGroupedBySight.entrySet()) {
      OrderSightEntry ose = new OrderSightEntry();
      ose.setOrder(o);
      ose.setSight(entry.getKey());
      em.persist(ose);

      List<Long> ticketsOfSight = entry.getValue().stream().map(Ticket::getId).collect(toList());
      Map<Date, List<Triplet<Long, Date, Integer>>> inSightGroupedByDate = triplets.stream()
          .filter(trip -> ticketsOfSight.contains(trip.first)).collect(groupingBy(t -> t.second));
      for (Map.Entry<Date, List<Triplet<Long, Date, Integer>>> inSightOnDate : inSightGroupedByDate
          .entrySet()) {
        if (!inSightOnDate.getValue().isEmpty()) {
          OrderDateEntry dateEntry = new OrderDateEntry();
          dateEntry.setDate(inSightOnDate.getKey());
          dateEntry.setSightEntry(ose);
          em.persist(dateEntry);

          for (Triplet<Long, Date, Integer> trip : inSightOnDate.getValue()) {
            Ticket ticket = ticketIdToObject.get(trip.first);
            OrderEntry oe = new OrderEntry();
            oe.setName(ticket.getName());
            oe.setQuantity(trip.third);
            oe.setUnitPrice(ticket.getPrice());
            oe.setDateEntry(dateEntry);
            oe.setExternalId(ticket.getExternalId());

            em.persist(oe);
          }
        }
      }
    }
    try {
      placeInExternalAPI(o);
    } catch (Exception e) {
      throw new ConflictingException("Nie udało się złożyć zamówienia w zewnętrznym systemie", e);
    }
    return o;
  }

  // em.refreshes are because of strange NPEs
  private void placeInExternalAPI(Order o) {
    em.refresh(o);
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be placed in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal =
        o.getEntries().stream().filter(ose -> ose.getSight().getPortal() != null)
            .collect(groupingBy(ose -> ose.getSight().getPortal()));
    for (Map.Entry<Portal, List<OrderSightEntry>> entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      List<OrderEntry> orderEntries = new ArrayList<>();
      for (OrderSightEntry se : entry.getValue()) {
        em.refresh(se);
        for (OrderDateEntry de : se.getEntries()) {
          em.refresh(de);
          orderEntries.addAll(de.getEntries());
        }
      }
      logger.log(Logger.Level.INFO, "Placing external order in " + portal.getName());
      Woo woo = new Woo(portal.getUrl(), portal.getKey(), portal.getSecret());
      Map<String, Object> resp = woo.placeOrder(o.getDetails(), orderEntries);
      logger.log(Logger.Level.INFO, resp.toString());
      Integer id = (Integer) resp.get("id");
      if (id != null) {
        entry.getValue().forEach(ose -> ose.setExternalId(id.longValue()));
      }
    }
  }

  @RolesAllowed("user")
  public List<OrderEntry> getOrderEntries() {
    return em.createQuery(
        "from OrderEntry oe join fetch oe.sightEntry ose join fetch ose.sight s join fetch ose.order o where o.user=:user order by oe.date asc",
        OrderEntry.class).setParameter("user", uService.me()).getResultList();
  }

  @RolesAllowed("user")
  public OrderEntry getOrderEntry(long id) {
    OrderEntry oe = em.find(OrderEntry.class, id);

    oe.getNumbers().size();
    return oe;
  }

  @RolesAllowed("user")
  public OrderDateEntry getOrderDateEntry(long id) {
    String queryString = "from OrderDateEntry where deleted=false and id=:id";
    OrderDateEntry de =
        em.createQuery(queryString, OrderDateEntry.class).setParameter("id", id).getSingleResult();

    de.getEntries().forEach(e -> e.getNumbers().size());
    return de;
  }

  @RolesAllowed("user")
  public List<OrderDateEntry> getOrderSightDateEntries() {
    List<OrderDateEntry> osdes = em.createQuery(
        "from OrderDateEntry osde join fetch osde.sightEntry ose join fetch ose.sight s join fetch ose.order o where osde.deleted=false and o.user=:user and o.status=:status order by osde.date asc",
        OrderDateEntry.class).setParameter("user", uService.me())
        .setParameter("status", Order.Status.CONFIRMED).getResultList();
    osdes.forEach(osde -> osde.getEntries().size());
    return osdes;
  }

  @RolesAllowed("user")
  public void deleteOrderDateEntry(long id) {
    em.find(OrderDateEntry.class, id).setDeleted(true);
  }

  @PermitAll
  public void ack(String hash, String ack) throws Exception {
    logger.log(Logger.Level.INFO, "Got ack from P24");
    Map<String, String> ackMap = PaymentUtils.queryToMap(ack);

    // TODO fill statement title or something
    // p24_order_id, p24_statement

    logger.log(Logger.Level.INFO, "confirming payment");
    StringBuilder signBuilder = new StringBuilder();
    signBuilder.append(ackMap.get("p24_session_id")).append("|");
    signBuilder.append(ackMap.get("p24_order_id")).append("|");
    signBuilder.append(ackMap.get("p24_amount")).append("|");
    signBuilder.append(ackMap.get("p24_currency")).append("|");
    signBuilder.append(properties.getProperty("przelewy24.crc"));
    String p24_sign = PaymentUtils.MD5(signBuilder.toString());

    ackMap.remove("p24_method");
    ackMap.remove("p24_statement");
    ackMap.put("p24_sign", p24_sign);

    URL url = new URL(properties.getProperty("przelewy24.confirmation.endpoint"));
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.addRequestProperty("Accept", MediaType.APPLICATION_FORM_URLENCODED);
    conn.addRequestProperty("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
    conn.setDoOutput(true);
    conn.getOutputStream().write(PaymentUtils.mapToQuery(ackMap).getBytes());
    logger.log(Logger.Level.INFO, "" + conn.getResponseCode());
    String resp = conn.getResponseMessage();
    logger.log(Logger.Level.INFO, resp);
    Order order = findByHash(hash);
    if (resp.equals("OK")) {
      logger.log(Logger.Level.INFO, "transaction confirmed. successful");
      confirm(order);
    } else {
      logger.log(Logger.Level.WARNING, "transaction problem.");
      problem(order);
    }
  }

  private void confirmInExternalAPI(Order order) {
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be confirmed in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal =
        order.getEntries().stream().filter(ose -> ose.getSight().getPortal() != null)
            .collect(groupingBy(ose -> ose.getSight().getPortal()));
    for (Map.Entry<Portal, List<OrderSightEntry>> entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Confirming external order in " + portal.getName());
      Woo woo = new Woo(portal.getUrl(), portal.getKey(), portal.getSecret());

      // they have same id. should have
      Long id = entry.getValue().stream().map(OrderSightEntry::getExternalId).findFirst().get();
      logger.log(Logger.Level.INFO, woo.completeOrder(id).toString());
    }
  }

  private void cancelInExternalAPI(Order order) {
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be cancelled in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal =
        order.getEntries().stream().filter(ose -> ose.getSight().getPortal() != null)
            .collect(groupingBy(ose -> ose.getSight().getPortal()));
    for (Map.Entry<Portal, List<OrderSightEntry>> entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Cancelling external order in " + portal.getName());
      Woo woo = new Woo(portal.getUrl(), portal.getKey(), portal.getSecret());

      // they have same id. should have
      Long id = entry.getValue().stream().map(OrderSightEntry::getExternalId).findFirst().get();
      logger.log(Logger.Level.INFO, woo.cancelOrder(id).toString());
    }
  }

  private Order findByHash(String hash) {
    return em.createQuery("from Order where hash=:hash", Order.class).setParameter("hash", hash)
        .getSingleResult();
  }

  @PermitAll
  public void cancel(Order o) {
    o.setStatus(Order.Status.CANCELLED);
    cancelInExternalAPI(o);
    // TODO send mail or something
  }

  private void problem(Order order) {
    order.setStatus(Status.PROBLEM);
    // TODO handle failure
  }

  private void confirm(Order order) {
    confirmInExternalAPI(order);
    order.setStatus(Status.CONFIRMED);

    // XXX Just for version 0.1. Will be deleted in further development
    for (OrderSightEntry ose : order.getEntries()) {
      for (OrderDateEntry ode : ose.getEntries()) {
        for (OrderEntry oe : ode.getEntries()) {
          for (int i = 0; i < oe.getQuantity(); i++) {
            oe.addNumber(order.getHash());
          }
        }
      }
    }
  }

}
