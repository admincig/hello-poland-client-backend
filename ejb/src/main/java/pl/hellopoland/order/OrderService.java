package pl.hellopoland.order;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.Ticket;
import pl.hellopoland.util.Triplet;

@LocalBean
@Stateless
public class OrderService extends ServiceSuperclass {

  @PermitAll
  public Order create(Collection<Triplet<Long, Date, Integer>> triplets, OrderDetails details) {

    Order o = new Order();
    o.generateHash();
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

    for (Map.Entry<Sight, List<Ticket>> entry : ticketsGroupedBySight.entrySet()) {
      OrderSightEntry ose = new OrderSightEntry();
      ose.setOrder(o);
      ose.setSight(entry.getKey());
      em.persist(ose);

      for (Ticket ticket : entry.getValue()) {
        for (Triplet<Long, Date, Integer> triplet : tripletsGroupedByTicketId.get(ticket.getId())) {
          OrderEntry oe = new OrderEntry();
          oe.setTicket(ticket);
          oe.setDate(triplet.second);
          oe.setQuantity(triplet.third);
          oe.setUnitPrice(ticket.getPrice());
          oe.setSightEntry(ose);
          em.persist(oe);
        }
      }
    }
    return o;
  }
}
