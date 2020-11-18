package pl.hellopoland.rest.helpdesk;

import java.io.File;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.service.api.helpdesk.ServiceHelpdeskAPI;

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
  @Operation(hidden = true)
  public Response rebuildSearchIndices() {
    service.rebuildSearchIndices();
    return Response.ok().build();
  }

  // TODO do usunięcia
  @GET
  @Path("/globalUserNameRefactor")
  @Operation(hidden = true)
  public Response globalUserNameRefactor() {
    service.globalRework();
    return Response.ok().build();
  }

  @GET
  @Path("/orders/{hash}/sudoAck")
  @Operation(hidden = true)
  public Response sudoAck(@PathParam("hash") String hash) {
    service.sudoAckOrder(hash);
    return Response.ok().build();
  }

}
