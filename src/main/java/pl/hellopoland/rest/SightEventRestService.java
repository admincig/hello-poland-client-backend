package pl.hellopoland.rest;

import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.dto.Push;
import pl.hellopoland.dto.SightEventDefinition;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.SightOnListingRO;
import pl.hellopoland.rest.dto.SightRO;
import pl.hellopoland.sight.SightEvent;
import pl.hellopoland.sight.SightEventService;
import pl.hellopoland.util.PagedEntityCollection;

@Path("/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SightEventRestService {

  @Inject
  SightEventService sightEventService;

  @GET
  @PermitAll
  public PagedCollection getList() {
    return search(new SightsPagedCollectionConfig());
  }

  @POST
  @Path("/")
  public void addToHpt(SightEventDefinition sightEvent) {
    sightEventService.addToHpt(sightEvent);
  }

  @POST
  public void savePush(Push push) {
    sightEventService.savePush(push.sightEvents);
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightsPagedCollectionConfig config) {
    PagedEntityCollection<SightEvent> plist = sightEventService.getList(config);
    return new PagedCollection(
        plist.items.stream().map(SightOnListingRO::new).collect(Collectors.toList()), plist.config);
  }

  @GET
  @Path("/{id}")
  public SightRO get(@PathParam("id") Long id) {
    return new SightRO(sightEventService.get(id));
  }
}
