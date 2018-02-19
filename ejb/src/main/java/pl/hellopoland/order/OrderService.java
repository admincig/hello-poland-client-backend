package pl.hellopoland.order;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.hellopoland.ConflictingException;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.sight.Portal;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.Ticket;
import pl.hellopoland.user.User;
import pl.hellopoland.user.UserService;
import pl.hellopoland.util.Triplet;
import pl.hellopoland.util.Woo;

@LocalBean
@Stateless
public class OrderService extends ServiceSuperclass {

  @Inject
  UserService uService;

  @PermitAll
  public Order create(Collection<Triplet<Long, Date, Integer>> triplets, OrderDetails details) {
    // DEVELOPER'S PURPOSES ONLY
    if (new Random().nextDouble() > 0.9) {
      throw new ConflictingException("Brak wolnych biletów na ten dzień");
    }

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

    Random random = new Random();

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
      o.addEntry(ose);

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
          ose.addEntry(dateEntry);

          for (Triplet<Long, Date, Integer> trip : inSightOnDate.getValue()) {
            Ticket ticket = ticketIdToObject.get(trip.first);
            OrderEntry oe = new OrderEntry();
            oe.setName(ticket.getName());
            oe.setQuantity(trip.third);
            oe.setUnitPrice(ticket.getPrice());
            oe.setDateEntry(dateEntry);
            oe.setExternalId(ticket.getExternalId());
            for (int i = 0; i < oe.getQuantity(); i++) {
              oe.addNumber("" + Math.abs(random.nextLong()));
            }
            em.persist(oe);
            dateEntry.addEntry(oe);
          }
        }
      }
    }
    placeInExternalAPI(o);
    return o;
  }

  private void placeInExternalAPI(Order o) {
    logger.info("Checking if any of order sight entries ought to be placed in external API");
    Map<Portal, List<OrderSightEntry>> groupedByPortal =
        o.getEntries().stream().filter(ose -> ose.getSight().getPortal() != null)
            .collect(groupingBy(ose -> ose.getSight().getPortal()));
    for (Map.Entry<Portal, List<OrderSightEntry>> entry : groupedByPortal.entrySet()) {
      Portal portal = entry.getKey();
      List<OrderEntry> orderEntries = new ArrayList<>();
      for (OrderSightEntry se : entry.getValue()) {
        for (OrderDateEntry de : se.getEntries()) {
          orderEntries.addAll(de.getEntries());
        }
      }
      logger.info("Placing external order in " + portal.getName());
      Woo woo = new Woo(portal.getUrl(), portal.getKey(), portal.getSecret());
      logger.info(woo.placeOrder(o.getDetails(), orderEntries).toString());
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
        "from OrderDateEntry osde join fetch osde.sightEntry ose join fetch ose.sight s join fetch ose.order o where osde.deleted=false and o.user=:user order by osde.date asc",
        OrderDateEntry.class).setParameter("user", uService.me()).getResultList();
    osdes.forEach(osde -> osde.getEntries().size());
    return osdes;
  }

  @RolesAllowed("user")
  public void deleteOrderDateEntry(long id) {
    em.find(OrderDateEntry.class, id).setDeleted(true);
  }
}
