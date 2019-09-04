package pl.hellopoland.service;

import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;

@Stateless
public class CategoryService extends ServiceSuperclass {

  @Inject
  private TranslationService tService;

  public Category create(CategoryDTO dto) {
    Category cat = new Category();
    cat.setLabel(dto.label);
    cat.setIconUrl(dto.iconUrl);
    cat.setDefaultLanguage(LanguageVersion.valueOf(dto.defaultLanguage));
    cat.setAvailableLanguageVersions(dto.availableLanguageVersions.stream()
        .map(LanguageVersion::valueOf).collect(Collectors.toSet()));
    em.persist(cat);
    tService.createEntityLanguageVersion(cat, dto, cat.getDefaultLanguage());
    return cat;
  }
}
