package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
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
  public PagedCollection get(@QueryParam("city") String city,
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
  public SightDTO favourite(@PathParam("id") Long id) {
    return service.favourite(id);
  }

  @GET
  @Path("/search")
  public PagedCollection search(@QueryParam("searchQuery") String searchQuery,
      @QueryParam("city") String city, @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    var config = new SightPagedCollectionConfig();
    config.onlyActive();
    config.onlyPublished();
    config.setSearchQuery(searchQuery);
    config.setCity(city);
    return service.getList(config, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightPagedCollectionConfig config, @QueryParam("city") String city,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    config.onlyActive();
    config.onlyPublished();
    config.setCity(city);
    return service.getList(config, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @GET
  @Path("/filters")
  public FiltersContainerDTO getFilters() {
    return filterService.getForSights();
  }

  @GET
  @Path("/recommended")
  public PagedCollection recommended(
      @HeaderParam("Content-Language") String contentLanguage,
      @QueryParam("count") @DefaultValue("6") Integer count) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.getRecommended(count, lang);
  }

}
