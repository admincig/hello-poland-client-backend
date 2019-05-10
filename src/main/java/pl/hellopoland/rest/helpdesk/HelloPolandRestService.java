package pl.hellopoland.rest.helpdesk;

import java.io.File;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
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

  @GET
  @Path("/sight-events")
  public PagedCollection getSightEvents(@HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.getSightEvents(new SightEventPagedCollectionConfig(),
        contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @PATCH
  @Path("/sight-events/{id}/promotion/{value}")
  public Response setSightEventPromotion(@PathParam("id") Long id,
      @PathParam("value") Integer promotion) {
    if (promotion.compareTo(1) < 0 || promotion.compareTo(3) > 0) {
      throw new ConflictingException("The 'value' parameter can be only 1 or 2 or 3.");
    }
    service.setSightEventPromotion(id, promotion);
    return Response.ok().build();
  }

  @DELETE
  @Path("/sight-events/{id}/promotion")
  public Response setSightEventPromotion(@PathParam("id") Long id) {
    service.removeSightEventPromotion(id);
    return Response.ok().build();
  }

  @GET
  @Path("/bookings/{p24Statement}/sendTicketCopy")
  public Response sendTicketCopy(@PathParam("p24Statement") String p24Statement) {
    service.sendTicketCopy(p24Statement);
    return Response.ok().build();
  }

}
