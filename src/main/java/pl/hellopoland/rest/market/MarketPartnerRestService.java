package pl.hellopoland.rest.market;

import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.market.PartnerServiceMarketAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
