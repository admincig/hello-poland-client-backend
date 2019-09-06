package pl.hellopoland.service.api.helpdesk;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.CategoryService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class CategoryServiceHelpdeskAPI {

  @Inject
  CategoryService service;
  @Inject
  TranslationService tService;

  @RolesAllowed("admin")
  public CategoryDTO create(CategoryDTO dto) {
    return DtoMapper.getFullDTO(service.create(dto));
  }

  @RolesAllowed("admin")
  public CategoryDTO createLanguageVesrion(CategoryDTO dto, LanguageVersion lang) {
    return DtoMapper.getFullDTO(service.createLanguageVesrion(dto, lang));
  }

  @RolesAllowed("admin")
  public PagedCollection pagedList(LanguageVersion language) {
    var config = new CategoryPagedCollectionConfig();
    var bos = service.pagedList(config);
    bos.items = tService.translateEntities(bos.items, language, false);
    List<CategoryDTO> dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public CategoryDTO get(Long id, LanguageVersion language) {
    Category cat = service.get(id);
    cat = tService.translateEntity(cat, language, true);
    return DtoMapper.getFullDTO(cat);
  }

  @RolesAllowed("admin")
  public CategoryDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    Category bo = service.get(id);
    if (!tService.isTranslated(bo, language)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + language.getLanuage()
              + "doesn't exists");
    }
    bo = service.changeDefaultLanguage(id, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public CategoryDTO update(CategoryDTO dto, LanguageVersion lang) {
    Category bo = service.update(dto, lang);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public void delete(long id) {
    service.delete(id);
  }

  @RolesAllowed("admin")
  public void deleteLanguageVersion(Long id, LanguageVersion lang) {
    service.deleteLanguageVersion(id, lang);
  }


}
