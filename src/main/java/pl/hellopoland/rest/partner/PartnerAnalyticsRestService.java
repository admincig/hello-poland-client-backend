package pl.hellopoland.rest.partner;

import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.service.api.partner.AnalyticsServicePartnerAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.io.File;
import java.util.Date;

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

  @GET
  @Path("/sales")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getSales(
            @QueryParam("fromDate") @DateFormat Date fromDate,
            @QueryParam("toDate") @DateFormat Date toDate
  ) {
        return Response.ok(service.getSales(fromDate, toDate)).build();
  }
}
