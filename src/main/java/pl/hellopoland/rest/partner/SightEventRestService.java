package pl.hellopoland.rest.partner;

import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDefinition;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.SightEventEventEventRO;
import pl.hellopoland.rest.dto.SightEventEventOnListingRO;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.sight.SightEvent;
import pl.hellopoland.sight.SightEventService;
import pl.hellopoland.util.PagedEntityCollection;

@Path("/partner/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SightEventRestService {

  @Inject
  SightEventService sightEventService;

  @Inject
  private CurrentUser currentUser;

  @GET
  @PermitAll
  public PagedCollection getList() {
    return search(new SightsPagedCollectionConfig());
  }

  @POST
  public Response addToHpt(SightEventDefinition sightEvent) {
    return Response.ok(sightEventService.addToHpt(sightEvent, currentUser)).build();
  }

  @PUT
  @Path("/{id}")
  public Response updatedInHpt(@PathParam("id") Long id, SightEventDefinition sightEvent) {
    return Response.ok(sightEventService.updateInHpt(id, sightEvent, currentUser)).build();
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightsPagedCollectionConfig config) {
    PagedEntityCollection<SightEvent> plist = sightEventService.getList(config);
    return new PagedCollection(
        plist.items.stream().map(SightEventEventOnListingRO::new).collect(Collectors.toList()),
        plist.config);
  }

  @GET
  @Path("/{id}")
  public SightEventEventEventRO get(@PathParam("id") Long id) {
    return new SightEventEventEventRO(sightEventService.get(id));
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    sightEventService.delete(id, currentUser);

    return Response.noContent().build();
  }
}
