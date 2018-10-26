package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.FilterMarketAPI;
import pl.hellopoland.service.api.market.SightServiceMarketAPI;

@Path("/market/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketSightRestService {

  @Inject
  private SightServiceMarketAPI service;

  @Inject
  private FilterMarketAPI filterService;

  @GET
  public PagedCollection get(@QueryParam("city") String city) {
    var config = new SightPagedCollectionConfig();
    config.onlyActive();
    config.onlyPublished();
    config.setCity(city);
    return service.getList(config);
  }

  @GET
  @Path("/{id}")
  public SightDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightPagedCollectionConfig config,
      @QueryParam("city") String city) {
    config.onlyActive();
    config.onlyPublished();
    config.setCity(city);
    return service.getList(config);
  }

  @GET
  @Path("/filters")
  public FiltersContainerDTO getFilters() {
    return filterService.getForSights();
  }

}
