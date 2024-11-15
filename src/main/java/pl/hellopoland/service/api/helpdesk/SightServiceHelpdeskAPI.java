package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class SightServiceHelpdeskAPI {

  @Inject
  private SightService service;
  @Inject
  private TranslationService tService;

  @RolesAllowed("admin")
  public PagedCollection<SightDTO> list(SightPagedCollectionConfig config,
      LanguageVersion language) {
    config.onlyActive();
    PagedEntityCollection<Sight> bos = service.getList(config, language);
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public void delete(Long id) {
    Sight bo = service.get(id);
    service.delete(bo);
  }

  @RolesAllowed("admin")
  public SightDTO update(SightDTO dto, LanguageVersion language) {
    Sight bo = service.get(dto.id);
    bo = service.update(bo, dto, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public SightDTO get(Long id, LanguageVersion language) {
    Sight bo = service.get(id);
    Set<Category> categories =
        bo.getSightEvents().stream().flatMap(se -> se.getCategories().stream())
            .map(SightEventCategory::getCategory).collect(Collectors.toSet());
    bo.setCategories(categories);
    bo = tService.translateEntity(bo, language);
    SightDTO dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("admin")
  public SightDTO createLanguageVesrion(SightDTO dto, LanguageVersion language) {
    Sight bo = service.createLanguageVersion(dto, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public SightDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    Sight bo = service.get(id);
    if (!tService.isTranslated(bo, language)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + language.getLanuage()
              + "doesn't exists");
    }
    bo = service.changeDefaultLanguage(bo, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public void deleteLanguageVersion(Long id, LanguageVersion language) {
    Sight bo = service.get(id);
    tService.deleteEntityTranslations(bo, language);
  }

}
