package pl.hellopoland.service.api.partner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class SightServicePartnerAPI {

  @Inject
  SightService service;
  @Inject
  TranslationService transService;

  @RolesAllowed("partner")
  public SightDTO create(SightDTO dto) {
    Sight bo = service.create(dto, null);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightDTO createLanguageVesrion(SightDTO dto, LanguageVersion language) {
    return DtoMapper.getFullDTO(service.createLanguageVersion(dto, language));
  }

  @RolesAllowed("partner")
  public PagedCollection getList(String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    List<Sight> bos = service.getActiveForPartner(language);
    var dtos = bos.stream().map(bo -> {
      var dto = DtoMapper.getDTO(bo);
      dto.language = bo.getDefaultLanguage().getLanuage();
      return dto;
    }).collect(Collectors.toList());
    if (language != null) {
      dtos.forEach(dto -> dto.language = language.getLanuage());
    }
    return new PagedCollection(dtos, null);
  }

  @RolesAllowed("partner")
  public SightDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion lang = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    Sight bo = service.getActiveForLoggedUser(id, lang);
    Set<Category> categories =
        bo.getSightEvents().stream().flatMap(se -> se.getCategories().stream())
            .map(SightEventCategory::getCategory).collect(Collectors.toSet());
    categories = new HashSet<>(transService.translateEntities(categories, lang, false));
    bo.setCategories(categories);
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightDTO update(SightDTO dto, LanguageVersion language) {
    Sight bo = service.updateForLoggedUser(dto, language);
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

  @RolesAllowed("partner")
  public SightDTO uploadMainImage(Long id, byte[] icon) {
    Sight bo = service.uploadMainImageForLoggedUser(id, icon);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightDTO uploadImage(Long id, byte[] icon) {
    Sight bo = service.addImageToSightGallery(id, icon);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightDTO removeImageFromGallery(Long id, Long imgId) {
    Sight bo = service.removeImageFromGallery(id, imgId);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    Sight bo = service.changeDefaultLanguage(id, language);
    return DtoMapper.getFullDTO(bo);
  }

}
