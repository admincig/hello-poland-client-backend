package pl.hellopoland.service.api.market;

import pl.hellopoland.rest.dto.PromotionCodeValidationIRO;
import pl.hellopoland.rest.dto.PromotionCodeValidationORO;
import pl.hellopoland.service.PromotionCodeService;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class PromotionCodeServiceMarketAPI {

  @Inject
  PromotionCodeService service;

  @PermitAll
  public PromotionCodeValidationORO validate(PromotionCodeValidationIRO iro) {
    return service.validate(iro);
  }
}
