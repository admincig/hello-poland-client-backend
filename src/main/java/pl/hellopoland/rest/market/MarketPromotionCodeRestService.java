package pl.hellopoland.rest.market;

import pl.hellopoland.rest.dto.PromotionCodeValidationIRO;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO;
import pl.hellopoland.rest.dto.PromotionCodeReservationReleaseIRO;
import pl.hellopoland.service.api.market.PromotionCodeServiceMarketAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/market/promotions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketPromotionCodeRestService {

  @Inject
  PromotionCodeServiceMarketAPI service;

  @POST
  @Path("/validate-code")
  public PromotionCodeValidationORO validate(PromotionCodeValidationIRO iro) {
    return service.validate(iro);
  }

  @POST
  @Path("/release-reservation")
  public void releaseReservation(PromotionCodeReservationReleaseIRO iro) {
    service.releaseReservation(iro);
  }
}
