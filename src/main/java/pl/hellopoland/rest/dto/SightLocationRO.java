package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.SightLocation;

public class SightLocationRO {

  public Double latitude;
  public Double longitude;
  public String street;
  public String zipCode;
  public String city;
  public String country;

  public SightLocationRO(SightLocation l) {
    this.latitude = l.getLatitude();
    this.longitude = l.getLongitude();
    this.street = l.getStreet();
    this.city = l.getCity();
    this.zipCode = l.getZipCode();
    this.country = l.getCountry();
  }
}
