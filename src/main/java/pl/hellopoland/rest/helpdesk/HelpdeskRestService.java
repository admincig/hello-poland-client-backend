package pl.hellopoland.rest.helpdesk;

//import io.swagger.v3.oas.annotations.Operation;
import pl.hellopoland.rest.dto.TicketEmailRequest;
import pl.hellopoland.service.api.helpdesk.ServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/helpdesk")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskRestService {

  @Inject
  private ServiceHelpdeskAPI service;

  @GET
  @Path("/bookings/{hash}/sendTicketCopy")
  public Response sendTicketCopy(@PathParam("hash") String hash) {
    service.sendTicketCopy(hash);
    return Response.ok().build();
  }

  @POST
  @Path("/bookings/{hash}/sendTicketCopyToEmail")
  public Response sendTicketCopyToEmail(@PathParam("hash") String hash,
      TicketEmailRequest request) {
    service.sendTicketCopyToEmail(hash, request == null ? null : request.email);
    return Response.ok().build();
  }

  @GET
  @Path("/rebuildSearchIndices")
//  @Operation(hidden = true)
  public Response rebuildSearchIndices() {
    service.rebuildSearchIndices();
    return Response.ok().build();
  }

  // TODO do usunięcia
  @GET
  @Path("/globalUserNameRefactor")
//  @Operation(hidden = true)
  public Response globalUserNameRefactor() {
    service.globalRework();
    return Response.ok().build();
  }

  @GET
  @Path("/orders/{hash}/sudoAck")
//  @Operation(hidden = true)
  public Response sudoAck(@PathParam("hash") String hash) {
    service.sudoAckOrder(hash);
    return Response.ok().build();
  }

}
