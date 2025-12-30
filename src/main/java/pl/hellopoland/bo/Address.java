package pl.hellopoland.bo;

import pl.hellopoland.annotation.Multilingual;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.Translated;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Address extends ModelSuperclass implements Translated {
  private static final long serialVersionUID = -1990557956651882571L;

  private String country;

  private String city;

  // format xx-xxx lub xxxxx
  private String postCode;

  private String street;

  @Column(length = 255)
  private String commune;      // gmina

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public void setVoivodeship(String voivodeship) {
        this.voivodeship = voivodeship;
    }

    @Column(length = 255)
  private String county;       // powiat
  @Column(length = 255)
  private String voivodeship;  // wojewodztwo

  @Multilingual
  @Column(length = 1000)
  private String directions;

  @Column(length = 5)
  @Enumerated(EnumType.STRING)
  private LanguageVersion defaultLanguage = LanguageVersion.PL_PL;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  private Set<LanguageVersion> availableLanguageVersions;

  @Transient
  private LanguageVersion currentLanguage;

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getDirections() {
    return directions;
  }

  public void setDirections(String directions) {
    this.directions = directions;
  }

  @Override
  public LanguageVersion getDefaultLanguage() {
    return defaultLanguage;
  }

  @Override
  public void setDefaultLanguage(LanguageVersion defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  @Override
  public Set<LanguageVersion> getAvailableLanguageVersions() {
    return availableLanguageVersions;
  }

  @Override
  public void setAvailableLanguageVersions(Set<LanguageVersion> availableLanguageVersions) {
    this.availableLanguageVersions = availableLanguageVersions;
  }

  @Override
  public boolean addAvailableLanguageVersion(LanguageVersion languageVersion) {
    if (availableLanguageVersions == null) {
      availableLanguageVersions = new HashSet<>();
    }
    return availableLanguageVersions.add(languageVersion);
  }

  @Override
  public boolean deleteAvailableLanguageVersion(LanguageVersion languageVersion) {
    if (availableLanguageVersions == null) {
      availableLanguageVersions = new HashSet<>();
    }
    return availableLanguageVersions.remove(languageVersion);
  }

  @Override
  public LanguageVersion getCurrentLanguage() {
    return currentLanguage;
  }

  @Override
  public void setCurrentLanguage(LanguageVersion currentLanguage) {
    this.currentLanguage = currentLanguage;
  }

}
