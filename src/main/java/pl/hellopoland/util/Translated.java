package pl.hellopoland.util;

import pl.hellopoland.enums.LanguageVersion;

import java.util.Set;

/**
 * This interface should be implemented by entities that will be translated into different
 * languages.
 */
public interface Translated {

  Long getId();

  LanguageVersion getDefaultLanguage();

  void setDefaultLanguage(LanguageVersion defaultLanguage);

  Set<LanguageVersion> getAvailableLanguageVersions();

  void setAvailableLanguageVersions(Set<LanguageVersion> availableLanguageVersions);

  boolean addAvailableLanguageVersion(LanguageVersion languageVersion);

  boolean deleteAvailableLanguageVersion(LanguageVersion languageVersion);

  LanguageVersion getCurrentLanguage();

  void setCurrentLanguage(LanguageVersion languageVersion);

}
