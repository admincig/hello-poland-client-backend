package pl.hellopoland.service.api.partner;

import pl.hellopoland.bo.Address;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class PartnerServicePartnerAPI {

  @Inject
  PartnerService service;
  @Inject
  TranslationService transService;

  @RolesAllowed("partner")
  public MarketPartnerDTO getCard(LanguageVersion parseLang) {
    Long loggedPartnerId = service.getLoggedPartner().getId();
    Partner partner = service.getPartnerWithCategoriesAndTagsAndCities(loggedPartnerId);
    partner.fetchRelations();
    Address address = transService.translateEntity(partner.getAddress(), parseLang);
    partner = transService.translateEntity(partner, parseLang);
    partner.setAddress(address);
    return DtoMapper.getFullMarketPartnerDTO(partner);
  }

  @RolesAllowed("partner")
  public MarketPartnerDTO uploadMainImage(byte[] icon) {
    Partner bo = service.getLoggedPartner();
    bo = service.uploadMainImage(bo, icon);
    return getCard(bo.getDefaultLanguage());
  }

  @RolesAllowed("partner")
  public void deleteLanguageVersion(LanguageVersion lang) {
    Partner bo = service.getLoggedPartner();
    transService.deleteEntityTranslations(bo, lang);
    transService.deleteEntityTranslations(bo.getAddress(), lang);
  }

  @RolesAllowed("partner")
  public MarketPartnerDTO changeDefaultLanguage(LanguageVersion lang) {
    Partner bo = service.getLoggedPartner();
    if (!transService.isTranslated(bo, lang)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + lang.getLanuage()
              + "doesn't exists");
    }
    bo = service.changeDefaultLanguage(bo, lang);
    return getCard(lang);
  }

  @RolesAllowed("partner")
  public MarketPartnerDTO update(MarketPartnerDTO dto, LanguageVersion lang) {
    Partner bo = service.getLoggedPartner();
    bo = service.update(bo, dto, lang);
    return getCard(lang);
  }

  @RolesAllowed("partner")
  public MarketPartnerDTO createLanguageVersion(MarketPartnerDTO dto, LanguageVersion language) {
    Partner bo = service.getLoggedPartner();
    dto.id = bo.getId();
    service.createLanguageVersion(dto, language);
    return getCard(language);
  }

}
