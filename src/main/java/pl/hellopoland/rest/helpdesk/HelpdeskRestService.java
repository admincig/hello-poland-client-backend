package pl.hellopoland.rest.helpdesk;

//import io.swagger.v3.oas.annotations.Operation;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.service.api.helpdesk.ServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.io.File;
import java.util.Date;

@RequestScoped
@Path("/helpdesk")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskRestService {

  @Inject
  private ServiceHelpdeskAPI service;

  @GET
  @Path("/analytics/orders")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response downloadOrdersCsv(@QueryParam("fromDate") @DateFormat Date fromDate,
      @QueryParam("toDate") @DateFormat Date toDate) {
    File report = service.getOrdersCsvFile(fromDate, toDate);
    ResponseBuilder response = Response.ok(report);
    response.header("Content-Disposition", "attachment;filename=" + report.getName());
    return response.build();
  }

  @GET
  @Path("/bookings/{hash}/sendTicketCopy")
  public Response sendTicketCopy(@PathParam("hash") String hash) {
    service.sendTicketCopy(hash);
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
