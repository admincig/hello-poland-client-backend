package pl.hellopoland.rest.helpdesk;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.ServiceHelpdeskAPI;

@RequestScoped
@Path("/helpdesk/sight-events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskSightEventRestService {

  @Inject
  private ServiceHelpdeskAPI service;

  @GET
  public PagedCollection getSightEvents(
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.getSightEvents(new SightEventPagedCollectionConfig(),
        HelpdeskRestService.parseLang(contentLanguage));
  }

  @DELETE
  @Path("/{id}/promotion")
  public Response setSightEventPromotion(
      @PathParam("id") Long id) {
    service.removeSightEventPromotion(id);
    return Response.ok().build();
  }

  @PATCH
  @Path("/{id}/promotion/{value}")
  public Response setSightEventPromotion(
      @PathParam("id") Long id,
      @PathParam("value") Integer promotion) {
    if (promotion.compareTo(1) < 0 || promotion.compareTo(3) > 0) {
      throw new ConflictingException("The 'value' parameter can be only 1 or 2 or 3.");
    }
    service.setSightEventPromotion(id, promotion);
    return Response.ok().build();
  }

}
