package pl.hellopoland.rest.market;

import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.FilterMarketAPI;
import pl.hellopoland.service.api.market.SightServiceMarketAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
  public PagedCollection<SightDTO> get(@QueryParam("city") String city,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    var config = new SightPagedCollectionConfig();
    config.onlyActive();
    config.onlyPublished();
    config.setCity(city);
    return service.getList(config, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @GET
  @Path("/{id}")
  public SightDTO get(@PathParam("id") Long id,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.get(id, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @PATCH
  @Path("/{id}/favourite")
  public SightDTO addToFavourite(@PathParam("id") Long id) {
    return service.addFavourite(id);
  }

  @DELETE
  @Path("/{id}/favourite")
  public Response removeFavourite(@PathParam("id") Long id) {
    service.removeFavourite(id);
    return Response.ok().build();
  }

  @GET
  @Path("/favourites")
  public PagedCollection<SightDTO> favourites(@HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    var config = new SightPagedCollectionConfig();
    config.onlyActive();
    config.onlyPublished();
    return service.favourites(config, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @GET
  @Path("/filters")
  public FiltersContainerDTO getFilters() {
    return filterService.getForSights();
  }

  @GET
  @Path("/recommended")
  public PagedCollection<SightDTO> recommended(
      @HeaderParam("Content-Language") String contentLanguage,
      @QueryParam("count") @DefaultValue("6") Integer count) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.getRecommended(count, lang);
  }

}
