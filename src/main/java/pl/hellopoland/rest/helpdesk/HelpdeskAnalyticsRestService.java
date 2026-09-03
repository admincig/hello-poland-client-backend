package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.service.AnalyticsService;

import jakarta.annotation.security.RolesAllowed;
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

@Path("/helpdesk/analytics")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class HelpdeskAnalyticsRestService {

    @Inject
    AnalyticsService analyticsService;

    @GET
    @Path("/orders")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
        "helpdesk_content_manager", "helpdesk_support"})
    public Response downloadOrdersCsv(@QueryParam("fromDate") @DateFormat Date fromDate,
                                      @QueryParam("toDate") @DateFormat Date toDate) {
        File report = analyticsService.getOrdersCsvFile(fromDate, toDate);
        ResponseBuilder response = Response.ok(report);
        response.header("Content-Disposition", "attachment;filename=" + report.getName());
        return response.build();
    }

    @GET
    @Path("/sales")
    @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
        "helpdesk_content_manager", "helpdesk_support"})
    public Response getSales(@QueryParam("fromDate") @DateFormat Date fromDate,
                             @QueryParam("toDate") @DateFormat Date toDate,
                             @QueryParam("partnerId") Long partnerId) {
        return Response.ok(analyticsService.getSales(fromDate, toDate, partnerId)).build();
    }
}
