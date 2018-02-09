package pl.hellopoland.rest.dto;

import pl.hellopoland.user.UserLocation;

public class UserLocationRO {
  public String city;
  public String country;

  public UserLocationRO(UserLocation l) {
    this.city = l.getCity();
    this.country = l.getCountry();
  }
}
