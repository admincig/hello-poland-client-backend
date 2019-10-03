package pl.hellopoland.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class CategoryService extends ServiceSuperclass {

  @Inject
  private TranslationService tService;

  public Category create(CategoryDTO dto) {
    Category cat = new Category();
    cat.setLabel(dto.label);
    cat.setIconUrl(dto.iconUrl);
    cat.setBackgroundUrl(dto.backgroundUrl);
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

  public Category get(long id) {
    return Optional.ofNullable(em.find(Category.class, id))
        .orElseThrow(() -> new ResourceNotFoundException());
  }

  public PagedEntityCollection<Category> pagedList(CategoryPagedCollectionConfig config) {
    List<Category> list = getQuery(config).getResultList();
    return new PagedEntityCollection<>(list, config);
  }

  public Category update(CategoryDTO dto, LanguageVersion lang) {
    Category bo = get(dto.id);
    if (bo.getDefaultLanguage().equals(lang)) {
      bo.setLabel(dto.label);
      bo.setRecommended(dto.recommended);
      bo.setRestricted(dto.restricted);
      bo.setIconUrl(dto.iconUrl);
      bo.setBackgroundUrl(dto.backgroundUrl);
      em.flush();
    }
    return tService.updateEntityLanguageVersion(bo, dto, lang);
  }

  public Category changeDefaultLanguage(Long id, LanguageVersion language) {
    Category bo = get(id);
    Category translation = tService.translateEntity(bo, language, true);
    bo.setDefaultLanguage(language);
    bo.setLabel(translation.getLabel());
    return em.merge(bo);
  }

  public void delete(Long id) {
    Category category = get(id);
    em.createQuery("delete from SightEventCategory where category=:category")
        .setParameter("category", category).executeUpdate();
    em.remove(category);
  }

  public void deleteLanguageVersion(Long id, LanguageVersion lang) {
    tService.deleteEntityTranslations(get(id), lang);
  }

  public List<SightEventCategory> getFor(List<SightEvent> sightEvents) {
    if (sightEvents.isEmpty()) {
      return Collections.emptyList();
    }
    return em
        .createQuery("from SightEventCategory where sightEvent in (:sightEvents)",
            SightEventCategory.class)
        .setParameter("sightEvents", sightEvents).getResultList();
  }
}
