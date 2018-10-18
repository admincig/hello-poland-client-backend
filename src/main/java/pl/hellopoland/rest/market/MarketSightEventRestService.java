package pl.hellopoland.rest.market;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.rest.dto.AvailableTicketNumberAssociationORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.FilterMarketAPI;
import pl.hellopoland.service.api.market.SightEventServiceMarketAPI;

@Path("/market/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketSightEventRestService {

  @Inject
  SightEventServiceMarketAPI service;

  @Inject
  private FilterMarketAPI filterService;

  @GET
  public PagedCollection getList(@QueryParam("city") String city,
      @HeaderParam("Accept-Language") String language) {
    var config = new SightEventPagedCollectionConfig();
    config.onlyActive();
    config.setCity(city);
    return service.getList(config, language);
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightEventPagedCollectionConfig config,
      @HeaderParam("Accept-Language") String language) {
    config.onlyActive();
    return service.getList(config, language);
  }

  @GET
  @Path("/{id}")
  public SightEventDTO get(@PathParam("id") Long id,
      @HeaderParam("Accept-Language") String language) {
    return service.get(id, language);
  }

  @GET
  @Path("/filters")
  public FiltersContainerDTO getFilters() {
    return filterService.getForSightEvents();
  }

  @GET
  @Path("/{id}/available-tickets")
  public AvailableTicketNumberAssociationORO checkAvailability(@PathParam("id") Long id,
      @QueryParam("date") @DateFormat final Date date) {
    return service.checkAvailability(id, date);
  }

}
