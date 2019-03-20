package pl.hellopoland.rest.helpdesk;

import java.io.File;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.service.api.hp.HellopolandServiceAPI;

@RequestScoped
@Path("/helpdesk")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloPolandRestService {
  @Inject
  private HellopolandServiceAPI service;

  @POST
  @Path("/partners")
  public PartnerDTO add(PartnerDTO partner) {
    return service.addPartner(partner);
  }

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

  @PATCH
  @Path("/sight-events/{id}/promotion/{value}")
  public Response setSightEventPromotion(@PathParam("id") Long id,
      @PathParam("value") Integer promotion) {
    service.setSightEventPromotion(id, promotion);
    return Response.ok().build();
  }

}
