package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class PartnerRepresentative extends ModelSuperclass {
  private static final long serialVersionUID = -6996932813336717031L;

  @NotBlank
  private String name;

  @NotBlank
  private Integer pesel;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPesel() {
    return pesel;
  }

  public void setPesel(Integer pesel) {
    this.pesel = pesel;
  }

}
