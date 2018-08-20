package pl.hellopoland.service.api.market;

import java.lang.System.Logger.Level;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.bo.P24PassageOrder;
import pl.hellopoland.rest.dto.OrderDateEntryORO;
import pl.hellopoland.rest.dto.OrderDateEntryOnListingORO;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.rest.dto.OrderORO;
import pl.hellopoland.service.OrderService;
import pl.hellopoland.util.Triplet;

@Stateless
public class OrderServiceMarketAPI {

  System.Logger logger = System.getLogger(OrderServiceMarketAPI.class.getName());

  @Inject
  OrderService service;

  @PermitAll
  public OrderORO create(OrderIRO iro) {
    Collection<Triplet<Long, Date, Integer>> tickets = iro.entries.stream()
        .map(e -> new Triplet<>(e.id, e.date, e.quantity)).collect(Collectors.toList());
    P24PassageOrder bo = service.create(tickets, iro.details);
    // Order bo = service.create(tickets, iro.details);
    var dto = new OrderORO(bo);
    return dto;
  }

  @PermitAll
  public void ack(String hash, String ack) {
    try {
      service.ack(hash, ack);
    } catch (Exception e) {
      logger.log(Level.WARNING, "Failed to ack payment", e);
    }
  }

  @RolesAllowed("user")
  public List<OrderDateEntryOnListingORO> getTicketGroupsForLoggedUser() {
    List<OrderDateEntry> bos = service.getOrderSightDateEntriesForLoggedUser();
    var dtos = bos.stream().map(OrderDateEntryOnListingORO::new).collect(Collectors.toList());
    return dtos;
  }

  @RolesAllowed("user")
  public OrderDateEntryORO getTicketGroup(Long id) {
    OrderDateEntry bo = service.getOrderDateEntryForLoggedUser(id);
    var dto = new OrderDateEntryORO(bo);
    return dto;
  }

  @RolesAllowed("user")
  public void deleteTicketGroup(Long id) {
    service.deleteOrderDateEntryForLoggedUser(id);
  }

  @PermitAll
  public void sudoAck(String hash) {
    service.sudoAck(hash);
  }
}
