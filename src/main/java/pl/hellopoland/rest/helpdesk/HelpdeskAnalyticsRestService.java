package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.service.AnalyticsService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;

@Path("/helpdesk/analytics")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class HelpdeskAnalyticsRestService {

    @Inject
    AnalyticsService service;

    @GET
    @Path("/sales")
    public Response getSales(@QueryParam("fromDate") @DateFormat Date fromDate,
                             @QueryParam("toDate") @DateFormat Date toDate,
                             @QueryParam("partnerId") Long partnerId) {
        return Response.ok(service.getSales(fromDate, toDate, partnerId)).build();
    }
}