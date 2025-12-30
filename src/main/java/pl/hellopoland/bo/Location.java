package pl.hellopoland.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Location {

  private Double latitude;
  private Double longitude;
  private String street;
  private String zipCode;
  private String city;
  private String country;

  @Column(length = 1000)
  private String directions;

  @Column(length = 255)
  private String commune;

  @Column(length = 255)
  private String county;

  @Column(length = 255)
  private String voivodeship;


  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getDirections() {
    return directions;
  }

  public void setDirections(String directions) {
    this.directions = directions;
  }

  public String getCommune() { return commune; }
  public void setCommune(String commune) {this.commune = commune; }
  public String getCounty() { return county; }
  public void setCounty(String county) { this.county = county; }
  public String getVoivodeship() { return voivodeship; }

    public void setVoivodeship(String voivodeship) {
        this.voivodeship = voivodeship;
    }


}
