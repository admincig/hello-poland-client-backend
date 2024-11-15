package pl.hellopoland.bo;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class HplInstance extends ModelSuperclass {

  @NotNull
  private String name;
  @NotNull
  private String url;
  private String tosUrl;
  private String privacyPolicyUrl;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTosUrl() {
    return tosUrl;
  }

  public void setTosUrl(String tosUrl) {
    this.tosUrl = tosUrl;
  }

  public String getPrivacyPolicyUrl() {
    return privacyPolicyUrl;
  }

  public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
    this.privacyPolicyUrl = privacyPolicyUrl;
  }
}
