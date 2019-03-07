package pl.hellopoland.enums;

public enum LanguageVersion {
  DE_DE("de-DE"), EN_GB("en-GB"), PL_PL("pl-PL");

  private String language;

  private LanguageVersion(String language) {
    this.language = language;
  }

  public static LanguageVersion getForCreateAndUpdateEntity(String language) {
    if (language == null || language.equals("")) {
      return null;
    }
    for (var lang : LanguageVersion.values()) {
      if (lang.language.equals(language)) {
        return lang;
      }
    }
    return null;
  }

  public static LanguageVersion getForTranslationEntity(String language) {
    if (language == null || language.equals("")) {
      return null;
    }
    for (var lang : LanguageVersion.values()) {
      if (lang.language.equals(language)) {
        return lang;
      }
      switch (language.substring(0, 2)) {
        case "de":
          return LanguageVersion.DE_DE;
        case "en":
          return LanguageVersion.EN_GB;
        case "pl":
          return LanguageVersion.PL_PL;
        default:
          return null;
      }
    }
    return null;
  }

  public String getLanuage() {
    return language;
  }
}
