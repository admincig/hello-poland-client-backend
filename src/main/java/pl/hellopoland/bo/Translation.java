package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "translation_key_language_unique",
    columnNames = {"key", "language"}))
public class Translation extends ModelSuperclass {
  private static final long serialVersionUID = 4888676435527982407L;
  public static final String KEY_DELIMITER = "|";

  public enum LanguageVersion {
    PL, EN;
  }

  @NotBlank
  private String key;
  private String value;
  @NotNull
  @Enumerated(EnumType.STRING)
  private LanguageVersion language;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public LanguageVersion getLanguage() {
    return language;
  }

  public void setLanguage(LanguageVersion language) {
    this.language = language;
  }

  @Transient
  public void generateKey(ModelSuperclass bo, String fieldName) {
    setKey(bo.getClass().getSimpleName() + KEY_DELIMITER + bo.getId() + KEY_DELIMITER + fieldName);
  }

  @Transient
  public void putLanguage(String language) {
    var langVersions = LanguageVersion.values();
    for (int i = 0; i < langVersions.length; i++) {
      if (langVersions[i].name().equals(language.toUpperCase())) {
        setLanguage(langVersions[i]);
        break;
      }
    }
  }

}
