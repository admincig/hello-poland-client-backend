package pl.hellopoland.service.api.market;

import pl.hellopoland.bo.Category;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.CategoryService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class CategoryServiceMarketAPI {

  @Inject
  CategoryService service;
  @Inject
  TranslationService tService;

  @PermitAll
  public PagedCollection<CategoryDTO> pagedList(LanguageVersion language) {
    var config = new CategoryPagedCollectionConfig();
    var bos = service.pagedList(config);
    bos.items = tService.translateEntities(bos.items, language);
    List<CategoryDTO> dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, bos.config);
  }

  @PermitAll
  public CategoryDTO get(Long id, LanguageVersion language) {
    Category cat = service.get(id);
    cat = tService.translateEntity(cat, language);
    return DtoMapper.getFullDTO(cat);
  }

}
