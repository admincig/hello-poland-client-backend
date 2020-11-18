package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class HplInstance extends ModelSuperclass {

  @NotNull
  private String name;
  @NotNull
  private String url;

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
}
