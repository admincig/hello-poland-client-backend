package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class PartnerServicePartnerAPI {

  @Inject
  PartnerService service;
  @Inject
  TranslationService transService;

  @RolesAllowed("partner")
  public MarketPartnerDTO getCard(LanguageVersion parseLang) {
    Long loggedPartnerId = service.getLoggedPartner().getId();
    Partner partner = service.getPartnerWithCategoriesAndCities(loggedPartnerId);
    transService.translateEntity(partner, parseLang, false);
    return DtoMapper.getFullMarketPartnerDTO(partner);
  }

  @RolesAllowed("partner")
  public MarketPartnerDTO uploadMainImage(Long id, byte[] icon) {
    Partner bo = service.getLoggedPartner();
    bo = service.uploadMainImage(bo, icon);
    return getCard(bo.getDefaultLanguage());
  }


}
