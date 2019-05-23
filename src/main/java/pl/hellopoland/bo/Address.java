package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Address extends ModelSuperclass {
  private static final long serialVersionUID = -1990557956651882571L;

  @NotBlank
  private String country;

  @NotBlank
  private String city;

  @NotBlank
  // format xx-xxx lub xxxxx
  private String postCode;

  @NotBlank
  private String street;

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

}
