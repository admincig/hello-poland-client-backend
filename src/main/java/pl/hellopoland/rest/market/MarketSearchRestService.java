package pl.hellopoland.rest.market;

import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.dto.FilterDTO;
import pl.hellopoland.dto.SearchResultDTO;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.service.api.market.SearchServiceMarketAPI;

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
    return service.search(
        RestService.parseLang(contentLanguage),
        query,
        categoryIds,
        tagIds,
        city,
        fromDate,
        toDate,
        minPrice,
        maxPrice);
  }

  @GET
  @Path("/filters")
  public FilterDTO filters(
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.filters(RestService.parseLang(contentLanguage));
  }
}
