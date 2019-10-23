package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.UserDetails;

public class UserDetailsRO {

  public String firstName;
  public String lastName;
  public String email;
  public String phone;
  public String street;
  public String zipCode;
  public String city;
  public String country;

  public UserDetailsRO(UserDetails details) {
    this.firstName = details.getFirstName();
    this.lastName = details.getLastName();
    this.phone = details.getPhone();
    this.street = details.getStreet();
    this.zipCode = details.getZipCode();
    this.city = details.getCity();
    this.country = details.getCountry();
  }
}
