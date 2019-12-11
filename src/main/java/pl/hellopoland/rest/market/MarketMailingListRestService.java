package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.service.api.market.MailingListServiceMarketAPI;

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
