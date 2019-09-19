package pl.hellopoland.rest.helpdesk;

import java.io.File;
import java.util.Date;
import java.util.Optional;
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
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
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
  @Path("/bookings/{p24Statement}/sendTicketCopy")
  public Response sendTicketCopy(@PathParam("p24Statement") String p24Statement) {
    service.sendTicketCopy(p24Statement);
    return Response.ok().build();
  }

  public static LanguageVersion parseLang(String contentLanguage) {
    if (StringUtils.isBlank(contentLanguage)) {
      throw new ConflictingException("Language is required");
    }
    return Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(contentLanguage))
        .orElseThrow(() -> new ConflictingException("Unsupported language: " + contentLanguage));
  }
}
