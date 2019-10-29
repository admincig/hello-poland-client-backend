package pl.hellopoland.bo;

import javax.persistence.Embeddable;
import pl.hellopoland.util.NameAndAddressSplitter;

@Embeddable
public class UserDetails {
  public UserDetails() {};

  public UserDetails(String name) {
    this.firstName = NameAndAddressSplitter.getFirstName(name);
    this.lastName = NameAndAddressSplitter.getLastName(name);
  }

  public UserDetails(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  private String firstName;
  private String lastName;
  private String phone;
  private String street;
  private String zipCode;
  private String city;
  private String country;
  private Boolean tosAgreement;

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

  public Boolean getTosAgreement() {
    return tosAgreement;
  }

  public void setTosAgreement(Boolean tosAgreement) {
    this.tosAgreement = tosAgreement;
  }


  public void update(UserDetails incoming) {
    this.setCity(incoming.getCity());
    this.setCountry(incoming.getCountry());
    this.setFirstName(incoming.getFirstName());
    this.setLastName(incoming.getLastName());
    this.setPhone(incoming.getPhone());
    this.setStreet(incoming.getStreet());
    this.setZipCode(incoming.getZipCode());
  }

  public void updateAgreements(UserDetails incoming) {
    if (incoming.tosAgreement != null) {
      this.setTosAgreement(incoming.getTosAgreement());
    }
  }

  public boolean hasAllRequiredAgreements() {
    return this.tosAgreement;
  }

}
