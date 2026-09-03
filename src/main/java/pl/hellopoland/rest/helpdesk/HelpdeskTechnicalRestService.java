package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.rest.dto.TechnicalRecoveryIRO;
import pl.hellopoland.service.TechnicalOperationsService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/helpdesk/technical")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("helpdesk_technical")
public class HelpdeskTechnicalRestService {

  @Inject
  private TechnicalOperationsService service;

  @GET
  @Path("/status")
  public Response status() {
    return Response.ok(service.getStatus()).build();
  }

  @GET
  @Path("/logs/{source}")
  public Response logs(@PathParam("source") String source) {
    return Response.ok(service.getLogs(source)).build();
  }

  @POST
  @Path("/recovery/wordpress")
  public Response recoverWordpress(TechnicalRecoveryIRO request) {
    String reason = request == null ? null : request.reason;
    return Response.status(Response.Status.ACCEPTED)
        .entity(service.requestWordpressRecovery(reason))
        .build();
  }

  @GET
  @Path("/operations")
  public Response recentOperations() {
    return Response.ok(service.getRecentOperations()).build();
  }

  @GET
  @Path("/operations/{requestId}")
  public Response operation(@PathParam("requestId") String requestId) {
    return Response.ok(service.getOperation(requestId)).build();
  }
}
