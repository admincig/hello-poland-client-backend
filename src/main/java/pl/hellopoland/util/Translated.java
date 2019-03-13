package pl.hellopoland.util;

import java.util.Set;
import pl.hellopoland.enums.LanguageVersion;

public interface Translated {
  LanguageVersion getDefaultLanguage();

  void setDefaultLanguage(LanguageVersion defaultLanguage);

  Set<LanguageVersion> getAvailableLanguageVersions();

  void setAvailableLanguageVersions(Set<LanguageVersion> availableLanguageVersions);

  boolean addAvailableLanguageVersion(LanguageVersion languageVersion);

  boolean deleteAvailableLanguageVersion(LanguageVersion languageVersion);
}
