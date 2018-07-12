package pl.hellopoland.service;

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
import java.util.Map.Entry;
import java.util.Set;
import javax.ejb.EJBAccessException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.Order.Status;
import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.bo.OrderDetails;
import pl.hellopoland.bo.OrderEntry;
import pl.hellopoland.bo.OrderSightEntry;
import pl.hellopoland.security.CurrentUser;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.Ticket;
import pl.hellopoland.bo.User;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PaymentUtils;
import pl.hellopoland.util.Triplet;

@LocalBean
@Stateless
public class OrderService extends ServiceSuperclass {

  @Inject
  UserService uService;

  public Order create(Collection<Triplet<Long, Date, Integer>> triplets, OrderDetails details) {
    User user = getLoggedUser();

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
    Map<SightEvent, List<Ticket>> ticketsGroupedBySight =
        tickets.stream().collect(groupingBy(Ticket::getSightEvent));
    Map<Long, Ticket> ticketIdToObject = tickets.stream().collect(toMap(Ticket::getId, t -> t));
    for (Map.Entry<SightEvent, List<Ticket>> entry : ticketsGroupedBySight.entrySet()) {
      OrderSightEntry ose = new OrderSightEntry();
      ose.setOrder(o);
      ose.setSightEvent(entry.getKey());
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
            oe.setExternalDefinitionId(ticket.getExternalId());

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
    var groupedByPortal =
        o.getEntries().stream().filter(ose -> ose.getSightEvent().getPortal() != null)
            .collect(groupingBy(ose -> ose.getSightEvent().getPortal()));
    for (var entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Placing external order in " + portal.getName());
      switch (portal.getType()) {
        case HELLOTICKET_CLOUD_1:
          placeInHpt(o.getDetails(), entry);
          break;
      }
    }
  }

  private void confirmInExternalAPI(Order o) {
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be confirmed in external API");
    var groupedByPortal =
        o.getEntries().stream().filter(ose -> ose.getSightEvent().getPortal() != null)
            .collect(groupingBy(ose -> ose.getSightEvent().getPortal()));
    for (var entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Confirming external order in " + portal.getName());

      switch (portal.getType()) {
        case HELLOTICKET_CLOUD_1:
          confirmInHpt(entry);
          break;
      }

    }
  }

  private void confirmInHpt(Map.Entry<Portal, List<OrderSightEntry>> entry) {
    Portal portal = entry.getKey();
    List<OrderEntry> orderEntries = gatherOrderEntries(entry.getValue());
    String serialNumber =
        entry.getValue().stream().map(OrderSightEntry::getSerialNumber).findFirst().get();
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    hpt.confirm(serialNumber, orderEntries);
  }

  private void placeInHpt(OrderDetails details, Entry<Portal, List<OrderSightEntry>> entry) {
    Portal portal = entry.getKey();
    List<OrderEntry> orderEntries = gatherOrderEntries(entry.getValue());

    HelloTicket hpt = new HelloTicket(portal.getUrl());
    var resp = hpt.book(details, orderEntries);
    Integer externalOrderId = resp.getInt("id");
    entry.getValue().forEach(ose -> ose.setExternalId(externalOrderId.longValue()));
  }

  private List<OrderEntry> gatherOrderEntries(List<OrderSightEntry> list) {
    List<OrderEntry> returnList = new ArrayList<>();
    for (OrderSightEntry se : list) {
      em.refresh(se);
      for (OrderDateEntry de : se.getEntries()) {
        em.refresh(de);
        returnList.addAll(de.getEntries());
      }
    }
    return returnList;
  }

  public OrderEntry getOrderEntry(long id) {
    OrderEntry oe = em.find(OrderEntry.class, id);

    oe.getNumbers().size();
    return oe;
  }

  public OrderDateEntry getOrderDateEntryForLoggedUser(long id) {
    String queryString = "from OrderDateEntry where deleted=false and id=:id and sightEntry.order.user=:user";
    OrderDateEntry de =
        em.createQuery(queryString, OrderDateEntry.class).setParameter("id", id)
            .setParameter("user", getLoggedUser()).getSingleResult();

    de.getEntries().forEach(e -> e.getNumbers().size());
    return de;
  }

  public List<OrderDateEntry> getOrderSightDateEntriesForLoggedUser() {
    List<OrderDateEntry> osdes = em.createQuery(
        "from OrderDateEntry osde join fetch osde.sightEntry ose join fetch ose.sightEvent s join fetch ose.order o where osde.deleted=false and o.user=:user and o.status=:status order by osde.date asc",
        OrderDateEntry.class).setParameter("user", getLoggedUser())
        .setParameter("status", Order.Status.CONFIRMED).getResultList();
    osdes.forEach(osde -> osde.getEntries().size());
    return osdes;
  }

  public void deleteOrderDateEntryForLoggedUser(long id) {
    getOrderDateEntryForLoggedUser(id).setDeleted(true);
  }

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

  private void cancelInExternalAPI(Order order) {
    logger.log(Logger.Level.INFO,
        "Checking if any of order sight entries ought to be cancelled in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal =
        order.getEntries().stream().filter(ose -> ose.getSightEvent().getPortal() != null)
            .collect(groupingBy(ose -> ose.getSightEvent().getPortal()));
    for (Map.Entry<Portal, List<OrderSightEntry>> entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      logger.log(Logger.Level.INFO, "Cancelling external order in " + portal.getName());

      // they have same id. should have
      /* Long id = */
      entry.getValue().stream().map(OrderSightEntry::getExternalId).findFirst().get();
      // TODO cancel in HPT
    }
  }

  private Order findByHash(String hash) {
    return em.createQuery("from Order where hash=:hash", Order.class).setParameter("hash", hash)
        .getSingleResult();
  }

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
  }

}
