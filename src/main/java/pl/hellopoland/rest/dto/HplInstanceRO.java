package pl.hellopoland.rest.dto;

public class HplInstanceRO {

  public String name;
  public String url;
  public String tosUrl;
  public String privacyPolicyUrl;

  public HplInstanceRO(String name, String url, String tosUrl, String privacyPolicyUrl) {
    this.name = name;
    this.url = url;
    this.tosUrl = tosUrl;
    this.privacyPolicyUrl = privacyPolicyUrl;
  }
}
