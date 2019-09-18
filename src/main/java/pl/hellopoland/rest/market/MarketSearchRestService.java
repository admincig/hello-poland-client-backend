package pl.hellopoland.rest.market;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.SearchResultORO;
import pl.hellopoland.service.api.market.SearchServiceMarketAPI;

@Path("/market/search")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketSearchRestService {

  @Inject
  SearchServiceMarketAPI service;

  @GET
  public SearchResultORO search(
      @HeaderParam("Content-Language") String contentLanguage,
      @QueryParam("query") String query,
      @QueryParam("categoryId") List<Long> categoryIds) {
    Optional<List<Long>> optCategories =
        categoryIds == null ? Optional.empty() : Optional.of(categoryIds);
    Optional<String> optQuery =
        query == null || query.isBlank() ? Optional.empty() : Optional.of(query);
    return service.search(RestService.parseLang(contentLanguage), optQuery, optCategories);
  }
}
