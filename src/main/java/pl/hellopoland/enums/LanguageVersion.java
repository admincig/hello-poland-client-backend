package pl.hellopoland.enums;

import java.util.Locale;
import java.util.Locale.LanguageRange;

public enum LanguageVersion {
  DE_DE("de-de"), EN_GB("en-gb"), PL_PL("pl-pl");

  private String language;

  private LanguageVersion(String language) {
    this.language = language;
  }

  /**
   * @return language value, e.g. "pl-pl" as "pl-PL"
   */
  public String getLanuage() {
    return language.substring(0, language.length() - 2)
        .concat(language.substring(language.length() - 2).toUpperCase());
  }

  public static LanguageVersion getForCreateAndUpdateEntity(String language) {
    if (language == null || language.equals("")) {
      return null;
    }
    var languageRange = Locale.LanguageRange.parse(language);
    for (LanguageRange range : languageRange) {
      for (var lang : LanguageVersion.values()) {
        if (lang.language.equals(range.getRange())) {
          return lang;
        }
      }
    }
    return null;
  }

  public static LanguageVersion getForTranslationEntity(String language) {
    if (language == null || language.equals("") || language.length() < 2) {
      return null;
    }
    var languageRange = Locale.LanguageRange.parse(language);
    for (LanguageRange range : languageRange) {
      for (var lang : LanguageVersion.values()) {
        if (lang.language.equals(range.getRange())) {
          return lang;
        }
      }
    }
    for (LanguageRange range : languageRange) {
      switch (range.getRange().substring(0, 2)) {
        case "de":
          return LanguageVersion.DE_DE;
        case "en":
          return LanguageVersion.EN_GB;
        case "pl":
          return LanguageVersion.PL_PL;
      }
    }
    return null;
  }

}
