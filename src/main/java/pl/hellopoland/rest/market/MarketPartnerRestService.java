package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.PartnerServiceMarketAPI;

@Path("/market/partners")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketPartnerRestService {

  @Inject
  PartnerServiceMarketAPI service;

  @GET
  public PagedCollection<MarketPartnerDTO> list(
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.list(RestService.parseLang(contentLanguage));
  }

  @GET
  @Path("/{id}")
  public MarketPartnerDTO getPartner(@PathParam("id") Long id,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.get(id, RestService.parseLang(contentLanguage));
  }

}
