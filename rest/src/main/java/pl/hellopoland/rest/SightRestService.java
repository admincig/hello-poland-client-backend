package pl.hellopoland.rest;

import java.util.Collection;
import java.util.stream.Collectors;
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
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.SightOnListingRO;
import pl.hellopoland.rest.dto.SightRO;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.SightService;
import pl.hellopoland.util.PagedEntityCollection;

@Path("/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SightRestService {

  @Inject
  SightService sightService;

  @GET
  public PagedCollection getList() {
    return search(new SightsPagedCollectionConfig());
  }

  @POST
  public void savePush(Collection<pl.hellopoland.dto.SightEventDefinition> sights) {
    sightService.savePush(sights);
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightsPagedCollectionConfig config) {
    PagedEntityCollection<Sight> plist = sightService.getList(config);
    return new PagedCollection(
        plist.items.stream().map(SightOnListingRO::new).collect(Collectors.toList()), plist.config);
  }

  @GET
  @Path("/{id}")
  public SightRO get(@PathParam("id") Long id) {
    return new SightRO(sightService.get(id));
  }
}
