package pl.hellopoland.rest.partner;

import pl.hellopoland.service.api.partner.OrderServicePartnerAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/partner/bookings")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerOrderRestService {

  @Inject
  private OrderServicePartnerAPI service;

  @GET
  @Path("/{hash}/sendTicketCopy")
  public Response sendTicketCopy(@PathParam("hash") String hash) {
    service.sendTicketCopy(hash);
    return Response.ok().build();
  }

}
