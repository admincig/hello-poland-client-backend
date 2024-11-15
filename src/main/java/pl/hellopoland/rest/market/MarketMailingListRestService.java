package pl.hellopoland.rest.market;

import pl.hellopoland.service.api.market.MailingListServiceMarketAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/market/mailing-list")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketMailingListRestService {

  @Inject
  MailingListServiceMarketAPI service;

  @POST
  @Path("/subscribe/{email}")
  public Response addToMailingList(@PathParam("email") String email) {
    String addedMail = service.addToMailingList(email);
    return Response
        .ok(Json.createObjectBuilder().add("addedEmail", addedMail).build())
        .build();
  }

}
