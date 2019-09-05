package pl.hellopoland.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class CategoryService extends ServiceSuperclass {

  @Inject
  private TranslationService tService;

  public PagedEntityCollection<Category> pagedList(CategoryPagedCollectionConfig config) {
    List<Category> list = getQuery(config).getResultList();
    return new PagedEntityCollection<>(list, config);
  }

  public Category create(CategoryDTO dto) {
    Category cat = new Category();
    cat.setLabel(dto.label);
    cat.setIconUrl(dto.iconUrl);
    cat.setPublished(Boolean.TRUE.equals(dto.published));
    cat.setRecommended(Boolean.TRUE.equals(dto.recommended));
    cat.setDefaultLanguage(LanguageVersion.valueOf(dto.defaultLanguage));
    cat.setAvailableLanguageVersions(dto.availableLanguageVersions.stream()
        .map(LanguageVersion::valueOf).collect(Collectors.toSet()));
    em.persist(cat);
    tService.createEntityLanguageVersion(cat, dto, cat.getDefaultLanguage());
    return cat;
  }

  public void delete(Long id) {
    em.remove(em.find(Category.class, id));
  }
}
