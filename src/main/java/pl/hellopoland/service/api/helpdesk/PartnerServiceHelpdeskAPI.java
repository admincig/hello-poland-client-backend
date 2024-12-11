package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.Address;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
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

  @RolesAllowed({"admin", "salesman"})
  public PartnerDTO addPartner(PartnerDTO partner) {
    return DtoMapper.getFullDTO(hplService.addPartner(partner));
  }

  @RolesAllowed("admin")
  public PagedCollection<PartnerDTO> listPartners(PartnerPagedCollectionConfig config,
      LanguageVersion language) {
    PagedEntityCollection<Partner> bos = service.getList(config);
    bos.items = transService.translateEntities(bos.items, language);
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public void deleteLanguageVersion(Long id, LanguageVersion lang) {
    Partner bo = service.get(id);
    transService.deleteEntityTranslations(bo, lang);
    transService.deleteEntityTranslations(bo.getAddress(), lang);
  }

  @RolesAllowed("admin")
  public PartnerDTO changeDefaultLanguage(Long id, LanguageVersion lang) {
    Partner bo = service.get(id);
    if (!transService.isTranslated(bo, lang)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + lang.getLanuage()
              + "doesn't exists");
    }
    service.changeDefaultLanguage(bo, lang);
    return get(id, lang);
  }

  @RolesAllowed("admin")
  public PartnerDTO get(Long id, LanguageVersion lang) {
    Partner bo = service.get(id);
    bo = transService.translateEntity(bo, lang);
    Address address = transService.translateEntity(bo.getAddress(), lang);
    Address correspondenceAddress = transService.translateEntity(bo.getCorrespondenceAddress(), lang);
    bo.setAddress(address);
    bo.setCorrespondenceAddress(correspondenceAddress);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public PartnerDTO update(PartnerDTO dto, LanguageVersion lang) {
    Partner bo = service.get(dto.id);
    service.update(bo, dto, lang);
    return get(dto.id, lang);
  }

  @RolesAllowed("admin")
  public PartnerDTO createLanguageVersion(PartnerDTO dto, LanguageVersion lang) {
    Partner bo = service.get(dto.id);
    dto.id = bo.getId();
    service.createLanguageVersion(dto, lang);
    return get(dto.id, lang);
  }

  @RolesAllowed("admin")
  public PartnerDTO uploadMainImage(Long id, byte[] icon) {
    Partner bo = service.get(id);
    service.uploadMainImage(bo, icon);
    return get(id, bo.getDefaultLanguage());
  }

  @RolesAllowed("admin")
  public PartnerDTO setBlocked(Long id, boolean blocked, LanguageVersion lang) {
    Partner bo = service.get(id);
    service.setBlocked(bo, blocked);
    return get(id, lang);
  }

}
