package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.Category;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
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
public class CategoryServiceHelpdeskAPI {

  @Inject
  CategoryService service;
  @Inject
  TranslationService tService;

  @RolesAllowed({"root", "admin"})
  public CategoryDTO create(CategoryDTO dto) {
    return DtoMapper.getFullDTO(service.create(dto));
  }

  @RolesAllowed({"root", "admin"})
  public CategoryDTO createLanguageVesrion(CategoryDTO dto, LanguageVersion lang) {
    return DtoMapper.getFullDTO(service.createLanguageVesrion(dto, lang));
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public PagedCollection<CategoryDTO> pagedList(LanguageVersion language) {
    var config = new CategoryPagedCollectionConfig();
    var bos = service.pagedList(config);
    bos.items = tService.translateEntities(bos.items, language);
    List<CategoryDTO> dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, bos.config);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public CategoryDTO get(Long id, LanguageVersion language) {
    Category cat = service.get(id);
    cat = tService.translateEntity(cat, language);
    return DtoMapper.getFullDTO(cat);
  }

  @RolesAllowed({"root", "admin"})
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

  @RolesAllowed({"root", "admin"})
  public CategoryDTO update(CategoryDTO dto, LanguageVersion lang) {
    Category bo = service.update(dto, lang);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed({"root", "admin"})
  public void reorder(List<Long> categoryIds) {
    service.reorder(categoryIds);
  }

  @RolesAllowed({"root", "admin"})
  public void delete(long id) {
    service.delete(id);
  }

  @RolesAllowed({"root", "admin"})
  public void deleteLanguageVersion(Long id, LanguageVersion lang) {
    service.deleteLanguageVersion(id, lang);
  }

  @RolesAllowed({"root", "admin"})
  public CategoryDTO uploadIcon(Long id, byte[] bytes, String extension) {
    Category category = service.uploadIcon(id, bytes, extension);
    return DtoMapper.getFullDTO(category);
  }

}
