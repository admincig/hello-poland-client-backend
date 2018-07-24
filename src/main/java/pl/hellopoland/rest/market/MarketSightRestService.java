package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.SightServiceMarketAPI;

@Path("/market/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketSightRestService {

  @Inject
  private SightServiceMarketAPI service;

  @GET
  public PagedCollection get() {
    return service.listActive();
  }

  @GET
  @Path("/{id}")
  public SightDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }
}
