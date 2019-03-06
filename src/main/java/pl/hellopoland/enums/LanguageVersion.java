package pl.hellopoland.enums;

public enum LanguageVersion {
  PL_PL("pl-PL"), DE_DE("de-DE"), EN_GB("en-GB");

  private String language;

  private LanguageVersion(String language) {
    this.language = language;
  }

  public static LanguageVersion getLanuageVersion(String language) {
    for (var lang : LanguageVersion.values()) {
      if (lang.language == language) {
        return lang;
      }
    }
    return null;
  }

  public String getLanuage() {
    return language;
  }
}
