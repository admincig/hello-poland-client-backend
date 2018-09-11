package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.SightEventServiceMarketAPI;

@Path("/market/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketSightEventRestService {

  @Inject
  SightEventServiceMarketAPI service;

  @GET
  public PagedCollection getList() {
    return search(new SightEventPagedCollectionConfig());
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightEventPagedCollectionConfig config) {
    config.onlyActive();
    return service.getList(config);
  }

  @GET
  @Path("/{id}")
  public SightEventDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }

}
