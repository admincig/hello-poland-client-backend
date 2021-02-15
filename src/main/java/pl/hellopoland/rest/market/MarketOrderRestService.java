package pl.hellopoland.rest.market;

import pl.hellopoland.dto.P24PassageCartDTO;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.service.api.market.OrderServiceMarketAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/market/orders")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketOrderRestService {

  @Inject
  OrderServiceMarketAPI service;

  @POST
  public P24PassageCartDTO create(OrderIRO iro) {
    return service.create(iro);
  }

  @POST
  @Path("/{hash}/ackPayment")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void ackPayment(@PathParam("hash") String hash, String ack) throws Exception {
    service.ack(hash, ack);
  }

  @GET
  @Path("/status/{hash}")
  public Response checkStatus(@PathParam("hash") String hash) {
    return Response.ok(
        Json.createObjectBuilder().add("order_status", service.checkStatus(hash).name()).build())
        .build();
  }

}
