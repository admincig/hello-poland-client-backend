package pl.hellopoland.bo;

import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.Translated;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "translation_key_language_unique",
    columnNames = {"key", "language"}))
public class Translation extends ModelSuperclass {
  private static final long serialVersionUID = 4888676435527982407L;
  public static final String KEY_DELIMITER = "|";

  @NotBlank
  private String key;
  @Column(columnDefinition = "varchar(2500)")
  private String value;
  @NotNull
  @Enumerated(EnumType.STRING)
  private LanguageVersion language;
  private boolean deleted;

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

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public void generateKey(Translated bo, String fieldName) {
    setKey(bo.getClass().getSimpleName() + KEY_DELIMITER + bo.getId() + KEY_DELIMITER + fieldName);
  }

}
