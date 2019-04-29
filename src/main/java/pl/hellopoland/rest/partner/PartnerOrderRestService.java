package pl.hellopoland.rest.partner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.service.api.partner.OrderServicePartnerAPI;

@Path("/partner/bookings")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerOrderRestService {

  @Inject
  private OrderServicePartnerAPI service;

  @GET
  @Path("/print/{p24Statement}")
  public Response printPurchsedTickets(@PathParam("p24Statement") String p24Statement) {
    service.sendTicketCopy(p24Statement);
    return Response.ok().build();
  }

}
