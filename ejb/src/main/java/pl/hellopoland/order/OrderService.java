package pl.hellopoland.order;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.hellopoland.ConflictingException;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.Ticket;
import pl.hellopoland.user.User;
import pl.hellopoland.user.UserService;
import pl.hellopoland.util.Triplet;

@LocalBean
@Stateless
public class OrderService extends ServiceSuperclass {

  @Inject
  UserService uService;

  @PermitAll
  public Order create(Collection<Triplet<Long, Date, Integer>> triplets, OrderDetails details) {
    // DEVELOPER'S PURPOSES ONLY
    if (new Random().nextDouble() > 0) {
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

    Map<Long, List<Triplet<Long, Date, Integer>>> tripletsGroupedByTicketId =
        triplets.stream().collect(Collectors.groupingBy(t -> t.first));
    Set<Long> ticketsIds = tripletsGroupedByTicketId.keySet();
    List<Ticket> tickets =
        em.createQuery("from Ticket t join fetch t.sight s where t.id in (:ids) order by s.id asc",
            Ticket.class).setParameter("ids", ticketsIds).getResultList();

    Map<Sight, List<Ticket>> ticketsGroupedBySight =
        tickets.stream().collect(Collectors.groupingBy(Ticket::getSight));

    Random random = new Random();
    for (Map.Entry<Sight, List<Ticket>> entry : ticketsGroupedBySight.entrySet()) {
      OrderSightEntry ose = new OrderSightEntry();
      ose.setOrder(o);
      ose.setSight(entry.getKey());
      em.persist(ose);

      for (Ticket ticket : entry.getValue()) {
        for (Triplet<Long, Date, Integer> triplet : tripletsGroupedByTicketId.get(ticket.getId())) {
          OrderEntry oe = new OrderEntry();
          oe.setTicket(ticket);
          oe.setName(ticket.getName());
          oe.setDate(triplet.second);
          oe.setQuantity(triplet.third);
          oe.setUnitPrice(ticket.getPrice());
          oe.setSightEntry(ose);
          for (int i = 0; i < oe.getQuantity(); i++) {
            oe.addNumber("" + Math.abs(random.nextLong()));
          }
          em.persist(oe);
        }
      }
    }
    // TODO place order in external API and throw ConflictingException when failed
    return o;
  }

  @RolesAllowed("user")
  public List<OrderEntry> getTickets() {
    List<OrderEntry> entries = em.createQuery(
        "from OrderEntry oe join fetch oe.sightEntry se join fetch se.order o join fetch se.sight s where o.user=:user order by oe.id desc",
        OrderEntry.class).setParameter("user", uService.me()).getResultList();
    entries.forEach(e -> e.getNumbers().size());
    return entries;
  }
}
