package pl.hellopoland.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class OrderDetails {

  public enum Platform {
    ANDROID, IOS, WEB, WEB_MOBILE, UNKNOWN;
  }


  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String street;
  private String zipCode;
  private String city;
  private String country;
  private String language;
  @Enumerated(EnumType.STRING)
  private Platform platform;
  private boolean userLogged;
  private boolean invoice;
  @Column(columnDefinition = "varchar")
  private String buyerNotes;
  @Column(length = 255)
  private String commune;

  @Column(length = 255)
  private String county;

  @Column(length = 255)
  private String voivodeship;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Platform getPlatform() {
    return platform;
  }

  public void setPlatform(Platform platform) {
    this.platform = platform;
  }

  public boolean isUserLogged() {
    return userLogged;
  }

  public void setUserLogged(boolean userLogged) {
    this.userLogged = userLogged;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public boolean getInvoice() {
    return invoice;
  }

  public void setInvoice(boolean invoice) {
    this.invoice = invoice;
  }

  public String getBuyerNotes() {
    return buyerNotes;
  }

  public void setBuyerNotes(String buyerNotes) {
    this.buyerNotes = buyerNotes;
  }

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
}
