package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.Address;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.HelpdeskAccessService;
import pl.hellopoland.service.HellopolandService;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.stream.Collectors;

@Stateless
public class PartnerServiceHelpdeskAPI {

  @Inject
  PartnerService service;
  @Inject
  HellopolandService hplService;
  @Inject
  TranslationService transService;
  @Inject
  HelpdeskAccessService accessService;

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager"})
  public PartnerDTO addPartner(PartnerDTO partner) {
    return DtoMapper.getFullDTO(hplService.addPartner(partner));
  }

  @RolesAllowed({"root", "admin"})
  public void resetPartner(Long id, String email) {
    hplService.resetPartnerCredentials(id, email);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public PagedCollection<PartnerDTO> listPartners(PartnerPagedCollectionConfig config,
      LanguageVersion language) {
    accessService.applyPartnerScope(config);
    PagedEntityCollection<Partner> bos = service.getList(config);
    bos.items = transService.translateEntities(bos.items, language);
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, bos.config);
  }

  @RolesAllowed({"root", "admin"})
  public void deleteLanguageVersion(Long id, LanguageVersion lang) {
    Partner bo = service.get(id);
    transService.deleteEntityTranslations(bo, lang);
    transService.deleteEntityTranslations(bo.getAddress(), lang);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager"})
  public PartnerDTO changeDefaultLanguage(Long id, LanguageVersion lang) {
    Partner bo = service.get(id);
    accessService.requirePartnerAccess(bo);
    if (!transService.isTranslated(bo, lang)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + lang.getLanuage()
              + "doesn't exists");
    }
    service.changeDefaultLanguage(bo, lang);
    return get(id, lang);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public PartnerDTO get(Long id, LanguageVersion lang) {
    Partner bo = service.get(id);
    accessService.requirePartnerAccess(bo);
    if (!bo.getDefaultLanguage().equals(lang)) {
          bo = transService.translateEntity(bo, lang);
          Address address = transService.translateEntity(bo.getAddress(), lang);
          Address correspondenceAddress = transService.translateEntity(bo.getCorrespondenceAddress(), lang);
          bo.setAddress(address);
          bo.setCorrespondenceAddress(correspondenceAddress);
    }
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager"})
  public PartnerDTO update(PartnerDTO dto, LanguageVersion lang) {
    Partner bo = service.get(dto.id);
    accessService.requirePartnerAccess(bo);
    String previousEmail = bo.getEmail();
    service.update(bo, dto, lang);
    hplService.synchronizePartnerWithHpt(service.get(dto.id), previousEmail);
    return get(dto.id, lang);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager"})
  public PartnerDTO createLanguageVersion(PartnerDTO dto, LanguageVersion lang) {
    Partner bo = service.get(dto.id);
    accessService.requirePartnerAccess(bo);
    dto.id = bo.getId();
    service.createLanguageVersion(dto, lang);
    return get(dto.id, lang);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager"})
  public PartnerDTO uploadMainImage(Long id, byte[] icon, String extension) {
    Partner bo = service.get(id);
    accessService.requirePartnerAccess(bo);
    service.uploadMainImage(bo, icon, extension);
    return get(id, bo.getDefaultLanguage());
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager"})
  public PartnerDTO setBlocked(Long id, boolean blocked, LanguageVersion lang) {
    Partner bo = service.get(id);
    accessService.requirePartnerAccess(bo);
    service.setBlocked(bo, blocked);
    return get(id, lang);
  }

}
