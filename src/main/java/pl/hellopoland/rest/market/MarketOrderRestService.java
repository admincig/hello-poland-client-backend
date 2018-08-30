package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.dto.P24PassageCartDTO;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.service.api.market.OrderServiceMarketAPI;

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

  @POST
  @Path("/{hash}/sudoAckPayment")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public void sudoAckPayment(@PathParam("hash") String hash) throws Exception {
    service.sudoAck(hash);
  }

  @GET
  @Path("/confirmation/{hash}")
  public Response checkConfirmation(@PathParam("hash") String hash) {
    return Response
        .ok(Json.createObjectBuilder().add("confirmed", service.checkConfirmation(hash)).build())
        .build();
  }

}
