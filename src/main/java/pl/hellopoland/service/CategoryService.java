package pl.hellopoland.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  public Category get(long id) {
    return em.find(Category.class, id);
  }

  public PagedEntityCollection<Category> pagedList(CategoryPagedCollectionConfig config) {
    List<Category> list = getQuery(config).getResultList();
    return new PagedEntityCollection<>(list, config);
  }

  public Category create(CategoryDTO dto) {
    Category cat = new Category();
    cat.setLabel(dto.label);
    cat.setIconUrl(dto.iconUrl);
    cat.setRestricted(Boolean.TRUE.equals(dto.restricted));
    cat.setRecommended(Boolean.TRUE.equals(dto.recommended));
    cat.setDefaultLanguage(LanguageVersion.getForCreateAndUpdateEntity(dto.language));
    cat.setAvailableLanguageVersions(new HashSet<>(Set.of(cat.getDefaultLanguage())));
    em.persist(cat);
    tService.createEntityLanguageVersion(cat, dto, cat.getDefaultLanguage());
    return cat;
  }

  public Category createLanguageVesrion(CategoryDTO dto, LanguageVersion language) {
    return tService.createEntityLanguageVersion(get(dto.id), dto, language);
  }

  public void delete(Long id) {
    em.remove(em.find(Category.class, id));
  }
}
