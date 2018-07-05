package pl.hellopoland.rest.market;

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
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.sight.SightEvent;
import pl.hellopoland.sight.SightEventService;
import pl.hellopoland.util.HplMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Path("/market/sight-events")
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
  @Path("/search")
  public PagedCollection search(SightsPagedCollectionConfig config) {
    config.onlyActive();
    PagedEntityCollection<SightEvent> plist = sightEventService.getList(config);
    return new PagedCollection(
        plist.items.stream().map(HplMapper::getDTO).collect(Collectors.toList()), plist.config);
  }

  @GET
  @Path("/{id}")
  public pl.hellopoland.dto.SightEvent get(@PathParam("id") Long id) {
    return HplMapper.getFullDTO(sightEventService.get(id));
  }
}
