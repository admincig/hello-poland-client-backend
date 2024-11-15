package pl.hellopoland.rest.market;

import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.FilterDTO;
import pl.hellopoland.dto.SearchResultDTO;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.service.api.market.SearchServiceMarketAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Date;

@Path("/market/search")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketSearchRestService {

  @Inject
  SearchServiceMarketAPI service;

  @GET
  public SearchResultDTO search(
      @QueryParam("query") String query,
      @QueryParam("categories") Long[] categoryIds,
      @QueryParam("tags") Long[] tagIds,
      @QueryParam("city") String city,
      @QueryParam("fromDate") @DateFormat Date fromDate,
      @QueryParam("toDate") @DateFormat Date toDate,
      @QueryParam("minPrice") Integer minPrice,
      @QueryParam("maxPrice") Integer maxPrice,
      @HeaderParam("Content-Language") String contentLanguage) {
    SightEventPagedCollectionConfig seConfig = new SightEventPagedCollectionConfig();
    seConfig.setLanguage(RestService.parseLang(contentLanguage));
    seConfig.setSearchQuery(query);
    seConfig.setCategoriesIdsArray(categoryIds);
    seConfig.setTagsIdsArray(tagIds);
    seConfig.setCity(city);
    if (fromDate == null) {
      fromDate = new Date();
    }
    seConfig.setDateFrom(fromDate);
    seConfig.setDateTo(toDate);
    return service.search(seConfig, minPrice, maxPrice);
  }

  @GET
  @Path("/filters")
  public FilterDTO filters(
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.filters(RestService.parseLang(contentLanguage));
  }
}
