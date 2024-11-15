package pl.hellopoland.service.api.partner;

import pl.hellopoland.bo.Category;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.CategoryService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class CategoryServicePartnerAPI {

  @Inject
  CategoryService service;
  @Inject
  TranslationService tService;

  @RolesAllowed("partner")
  public PagedCollection<CategoryDTO> pagedList(LanguageVersion language) {
    var config = new CategoryPagedCollectionConfig();
    var bos = service.pagedList(config);
    bos.items = tService.translateEntities(bos.items, language);
    List<CategoryDTO> dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, bos.config);
  }

  @RolesAllowed("partner")
  public CategoryDTO get(Long id, LanguageVersion language) {
    Category cat = service.get(id);
    cat = tService.translateEntity(cat, language);
    return DtoMapper.getFullDTO(cat);
  }

}
