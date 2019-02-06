package pl.hellopoland.rest.partner;

import java.io.File;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.service.api.partner.AnalyticsServicePartnerAPI;

@Path("/partner/analytics")
@RequestScoped
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class PartnerAnalyticsRestService {

  @Inject
  AnalyticsServicePartnerAPI service;

  @GET
  @Path("/orders")
  public Response downloadOrdersCsv(@QueryParam("fromDate") @DateFormat Date fromDate,
      @QueryParam("toDate") @DateFormat Date toDate) {
    File report = service.getOrdersCsvFile(fromDate, toDate);
    ResponseBuilder response = Response.ok(report);
    response.header("Content-Disposition", "attachment;filename=" + report.getName());
    return response.build();
  }
}
