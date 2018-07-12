package pl.hellopoland.rest.market;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.service.OrderService;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.rest.dto.OrderORO;
import pl.hellopoland.security.CurrentUser;
import pl.hellopoland.service.api.market.OrderServiceMarketAPI;
import pl.hellopoland.util.Triplet;

@Path("/market/orders")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketOrderRestService {

  @Inject
  OrderServiceMarketAPI service;

  @Inject
  private CurrentUser currentUser;

  @POST
  public OrderORO create(OrderIRO iro) {
    return service.create(iro);
  }

  @POST
  @Path("/{hash}/ackPayment")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void ackPayment(@PathParam("hash") String hash, String ack) throws Exception {
    service.ack(hash, ack);
  }

}
