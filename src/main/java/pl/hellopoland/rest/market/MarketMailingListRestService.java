package pl.hellopoland.rest.market;

import pl.hellopoland.service.api.market.MailingListServiceMarketAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
