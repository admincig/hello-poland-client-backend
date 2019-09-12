package pl.hellopoland.service.api.partner;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightEventServicePartnerAPI {

  @Inject
  SightEventService service;

  @RolesAllowed("partner")
  public SightEventDTO create(SightEventDTO dto) {
    SightEvent bo = service.create(dto, null);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightEventDTO createLanguageVesrion(SightEventDTO dto, LanguageVersion language) {
    return DtoMapper.getFullDTO(service.createLanguageVersionForLoggedUser(dto, language));
  }

  @RolesAllowed("partner")
  public SightEventDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion lang = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    SightEvent bo = service.getForLoggedUser(id, lang);
    var dto = DtoMapper.getFullDTO(bo);
    if (lang == null) {
      lang = bo.getDefaultLanguage();
    }
    dto.language = lang.getLanuage();
    return dto;
  }

  @RolesAllowed("partner")
  public PagedCollection getList(SightEventPagedCollectionConfig config,
      String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    config.onlyCurrentPartner(true);
    config.onlyActive();
    PagedEntityCollection<SightEvent> bos = service.getList(config, language);
    var dtos = bos.items.stream().map(bo -> {
      var dto = DtoMapper.getDTO(bo);
      dto.language = bo.getDefaultLanguage().getLanuage();
      return dto;
    }).collect(Collectors.toList());
    if (language != null) {
      dtos.forEach(dto -> dto.language = language.getLanuage());
    }
    service.fetchTicketPoolDefinitions(bos.items, dtos, true);
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("partner")
  public SightEventDTO update(SightEventDTO dto, LanguageVersion language) {
    SightEvent bo = service.updateForLoggedUser(dto, language);
    return DtoMapper.getFullDTO(bo);
  }


  @RolesAllowed("partner")
  public SightEventDTO uploadMainImage(Long id, byte[] icon) {
    SightEvent bo = service.uploadMainImageForLoggedUser(id, icon);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true);
    return dto;
  }

  @RolesAllowed("partner")
  public SightEventDTO uploadImage(Long id, byte[] img) {
    SightEvent bo = service.addImageToSightEventGallery(id, img);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true);
    return dto;
  }

  @RolesAllowed("partner")
  public SightEventDTO removeImageFromGallery(Long id, Long imgId) {
    SightEvent bo = service.removeImageFromGallery(id, imgId);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightEventDTO uploadPdf(Long id, byte[] pdf) {
    SightEvent bo = service.uploadPdf(id, pdf);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true);
    return dto;
  }

  @RolesAllowed("partner")
  public void deletePdf(Long id) {
    service.deletePdf(id);
  }

  @RolesAllowed("partner")
  public void stopSale(Long sightId, Long ticketPoolDefId, Date date) {
    service.stopSale(sightId, ticketPoolDefId, date);
  }

  @RolesAllowed("partner")
  public SightEventDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    SightEvent bo = service.changeDefaultLanguageForLoggedUser(id, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.deleteForLoggedUser(id);
  }

  @RolesAllowed("partner")
  public void delete(Long id, LanguageVersion language) {
    service.deleteForLoggedUser(id, language);
  }

}
