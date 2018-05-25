package pl.hellopoland.rest;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.order.OrderService;
import pl.hellopoland.rest.dto.OrderDateEntryORO;
import pl.hellopoland.rest.dto.OrderDateEntryOnListingORO;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.rest.dto.OrderORO;
import pl.hellopoland.util.Triplet;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderRestService {

  @Inject
  OrderService orderService;

  @POST
  @Path("/orders")
  public OrderORO create(OrderIRO iro) {
    Collection<Triplet<Long, Date, Integer>> tickets = iro.entries.stream()
        .map(e -> new Triplet<>(e.id, e.date, e.quantity)).collect(Collectors.toList());
    return new OrderORO(orderService.create(tickets, iro.details));
  }

  @POST
  @Path("/orders/{hash}/ackPayment")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void ackPayment(@PathParam("hash") String hash, String ack) throws Exception {
    orderService.ack(hash, ack);
  }

  @GET
  @Path("/tickets")
  public List<OrderDateEntryOnListingORO> tickets() {
    return orderService.getOrderSightDateEntries().stream().map(OrderDateEntryOnListingORO::new)
        .collect(Collectors.toList());
  }

  @GET
  @Path("/tickets/{id}")
  public OrderDateEntryORO ticket(@PathParam("id") Long id) {
    return new OrderDateEntryORO(orderService.getOrderDateEntry(id));
  }

  @DELETE
  @Path("/tickets/{id}")
  public void deleteTicket(@PathParam("id") Long id) {
    orderService.deleteOrderDateEntry(id);
  }
}
