package pl.hellopoland.service;

import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.*;

@Stateless
public class CategoryService extends ServiceSuperclass {

  @Inject
  private TranslationService tService;
  @Inject
  private ImageService iService;

  public Category create(CategoryDTO dto) {
    Category cat = new Category();
    cat.setLabel(dto.label);
    cat.setIconUrl(dto.iconUrl);
    cat.setBackgroundUrl(dto.backgroundUrl);
    cat.setRestricted(Boolean.TRUE.equals(dto.restricted));
    cat.setRecommended(Boolean.TRUE.equals(dto.recommended));
    cat.setDisplayOrder(dto.displayOrder);
    cat.setDefaultLanguage(LanguageVersion.getForCreateAndUpdateEntity(dto.language));
    cat.setAvailableLanguageVersions(new HashSet<>(Set.of(cat.getDefaultLanguage())));
    em.persist(cat);
    tService.createEntityLanguageVersion(cat, dto, cat.getDefaultLanguage());
    normalizeDisplayOrder(cat, dto.displayOrder);
    return cat;
  }

  public Category createLanguageVesrion(CategoryDTO dto, LanguageVersion language) {
    return tService.createEntityLanguageVersion(get(dto.id), dto, language);
  }

  public Category get(long id) {
    Category cat = em.find(Category.class, id);
    return Optional.ofNullable(cat).map(c -> {
      c.getAvailableLanguageVersions().size();
      return c;
    }).orElseThrow(() -> new ResourceNotFoundException());
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
      if (dto.displayOrder != null) {
        normalizeDisplayOrder(bo, dto.displayOrder);
      }
      em.flush();
    }
    return tService.updateEntityLanguageVersion(bo, dto, lang);
  }

  public void reorder(List<Long> categoryIds) {
    List<Category> categories = getDisplayOrderedCategories();
    Map<Long, Category> categoriesById = new HashMap<>();
    categories.forEach(category -> categoriesById.put(category.getId(), category));

    List<Category> reorderedCategories = new ArrayList<>();
    if (categoryIds != null) {
      for (Long categoryId : categoryIds) {
        Category category = categoriesById.remove(categoryId);
        if (category != null) {
          reorderedCategories.add(category);
        }
      }
    }

    reorderedCategories.addAll(categories.stream()
        .filter(category -> categoriesById.containsKey(category.getId()))
        .toList());

    setDisplayOrder(reorderedCategories);
  }

  public Category changeDefaultLanguage(Long id, LanguageVersion language) {
    Category bo = get(id);
    Category translation = tService.translateEntity(bo, language);
    bo.setDefaultLanguage(language);
    bo.setLabel(translation.getLabel());
    return em.merge(bo);
  }

  public void delete(Long id) {
    Category category = get(id);
    em.createQuery("delete from SightEventCategory where category=:category")
        .setParameter("category", category).executeUpdate();
    em.remove(category);
    em.flush();
    normalizeDisplayOrder(null, null);
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

  public Category uploadIcon(Long id, byte[] bytes, String extension) {
    Category category = get(id);
    ImageCollector ic =
        iService.storeImageCollector(new ByteArrayInputStream(bytes), extension);
    category.setIconUrl(ic.getQvga().getDownloadUrl());
    return category;
  }

  private void normalizeDisplayOrder(Category movedCategory, Integer requestedDisplayOrder) {
    List<Category> categories = getDisplayOrderedCategories();

    if (movedCategory != null) {
      categories.removeIf(category -> category.getId().equals(movedCategory.getId()));
      int requestedIndex = requestedDisplayOrder == null
          ? categories.size()
          : Math.max(0, Math.min(requestedDisplayOrder - 1, categories.size()));
      categories.add(requestedIndex, movedCategory);
    }

    setDisplayOrder(categories);
  }

  private List<Category> getDisplayOrderedCategories() {
    return em.createQuery(
        "from Category c order by case when c.displayOrder is null then 1 else 0 end, "
            + "c.displayOrder asc, c.id desc",
        Category.class).getResultList();
  }

  private void setDisplayOrder(List<Category> categories) {
    for (int i = 0; i < categories.size(); i++) {
      categories.get(i).setDisplayOrder(i + 1);
    }
  }

}
