package pl.hellopoland.util;

import java.util.Set;
import pl.hellopoland.enums.LanguageVersion;

/**
 * This interface should be implemented by entities that will be translated into different
 * languages.
 */
public interface Translated {
  LanguageVersion getDefaultLanguage();

  void setDefaultLanguage(LanguageVersion defaultLanguage);

  Set<LanguageVersion> getAvailableLanguageVersions();

  void setAvailableLanguageVersions(Set<LanguageVersion> availableLanguageVersions);

  boolean addAvailableLanguageVersion(LanguageVersion languageVersion);

  boolean deleteAvailableLanguageVersion(LanguageVersion languageVersion);
}
