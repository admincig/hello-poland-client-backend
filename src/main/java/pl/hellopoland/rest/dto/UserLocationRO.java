package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.UserDetails;

public class UserLocationRO {

  public String city;
  public String country;

  public UserLocationRO(UserDetails details) {
    this.city = details.getCity();
    this.country = details.getCountry();
  }
}
