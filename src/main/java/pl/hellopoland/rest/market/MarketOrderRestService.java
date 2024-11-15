package pl.hellopoland.rest.market;

import pl.hellopoland.dto.P24PassageCartDTO;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.service.api.market.OrderServiceMarketAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
