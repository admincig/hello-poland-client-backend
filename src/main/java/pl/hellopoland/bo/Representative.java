package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Representative {
  @NotBlank
  private String name;

  @NotBlank
  private String pesel;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPesel() {
    return pesel;
  }

  public void setPesel(String pesel) {
    this.pesel = pesel;
  }

}
