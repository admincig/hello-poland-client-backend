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
  public PagedCollection pagedList(String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    var config = new CategoryPagedCollectionConfig();
    var bos = service.pagedList(config);
    bos.items = tService.translateEntities(bos.items, language, false);
    List<CategoryDTO> dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public void delete(long id) {
    service.delete(id);
  }

  @RolesAllowed("admin")
  public CategoryDTO createLanguageVesrion(CategoryDTO dto, LanguageVersion lang) {
    return DtoMapper.getFullDTO(service.createLanguageVesrion(dto, lang));
  }

  @RolesAllowed("admin")
  public CategoryDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    Category cat = service.get(id);
    cat = tService.translateEntity(cat, language, true);
    return DtoMapper.getFullDTO(cat);
  }

}
