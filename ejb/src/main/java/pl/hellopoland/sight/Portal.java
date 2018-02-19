package pl.hellopoland.sight;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;

@Entity
public class Portal extends ModelSuperclass {
  private static final long serialVersionUID = -6636726122893941989L;

  @NotNull
  private String name;
  @NotNull
  private String url;
  private String key;
  private String secret;

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

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }


}
