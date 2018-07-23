package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.UserLocation;

public class UserLocationRO {

  public String city;
  public String country;

  public UserLocationRO(UserLocation l) {
    this.city = l.getCity();
    this.country = l.getCountry();
  }
}
