package pl.hellopoland.rest.partner;

import pl.hellopoland.service.api.partner.OrderServicePartnerAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
