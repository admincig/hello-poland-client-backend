package pl.hellopoland.service.api.market;

import java.lang.System.Logger.Level;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Order.Status;
import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.dto.P24PassageCartDTO;
import pl.hellopoland.rest.dto.OrderDateEntryORO;
import pl.hellopoland.rest.dto.OrderDateEntryOnListingORO;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.service.OrderService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class OrderServiceMarketAPI {

  System.Logger logger = System.getLogger(OrderServiceMarketAPI.class.getName());

  @Inject
  OrderService service;

  @PermitAll
  public P24PassageCartDTO create(OrderIRO iro) {
    return DtoMapper.getDTO(service.create(iro));
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
  public List<OrderDateEntryOnListingORO> getActiveTicketGroupsForLoggedUser() {
    List<OrderDateEntry> bos = service.getOrderSightDateEntriesInTheFutureForLoggedUser();
    return bos.stream().map(OrderDateEntryOnListingORO::new).collect(Collectors.toList());
  }

  @RolesAllowed("user")
  public List<OrderDateEntryOnListingORO> getArchivedTicketGroupsForLoggedUser() {
    List<OrderDateEntry> bos = service.getOrderSightDateEntriesInThePastForLoggedUser();
    return bos.stream().map(OrderDateEntryOnListingORO::new).collect(Collectors.toList());
  }

  @RolesAllowed("user")
  public OrderDateEntryORO getTicketGroup(Long id) {
    OrderDateEntry bo = service.getOrderDateEntryForLoggedUser(id);
    return new OrderDateEntryORO(bo);
  }

  @RolesAllowed("user")
  public void deleteTicketGroup(Long id) {
    service.deleteOrderDateEntryForLoggedUser(id);
  }

  @PermitAll
  public Status checkStatus(String hash) {
    return service.getStatus(hash);
  }
}
