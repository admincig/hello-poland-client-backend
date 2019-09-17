package pl.hellopoland.rest.market;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
  @Path("/{id}")
  public SearchResultORO search(@HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    return service.search(List.of(id), RestService.parseLang(contentLanguage));
  }
}
