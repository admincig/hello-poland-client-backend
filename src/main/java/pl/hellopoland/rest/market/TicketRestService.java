package pl.hellopoland.rest.market;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.order.OrderService;
import pl.hellopoland.rest.dto.OrderDateEntryORO;
import pl.hellopoland.rest.dto.OrderDateEntryOnListingORO;
import pl.hellopoland.security.dto.CurrentUser;

@Path("/market/tickets")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketRestService {

  @Inject
  OrderService orderService;

  @Inject
  private CurrentUser currentUser;

  @GET
  @RolesAllowed("user")
  public List<OrderDateEntryOnListingORO> tickets() {
    return orderService.getOrderSightDateEntries(currentUser).stream()
        .map(OrderDateEntryOnListingORO::new)
        .collect(Collectors.toList());
  }

  @GET
  @Path("/{id}")
  @RolesAllowed("user")
  public OrderDateEntryORO ticket(@PathParam("id") Long id) {
    return new OrderDateEntryORO(orderService.getOrderDateEntry(id));
  }

  @DELETE
  @Path("/{id}")
  @RolesAllowed("user")
  public void deleteTicket(@PathParam("id") Long id) {
    orderService.deleteOrderDateEntry(id);
  }
}
