package pl.hellopoland.bo;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class PartnerRepresentative extends ModelSuperclass {
  private static final long serialVersionUID = -6996932813336717031L;

  @NotBlank
  private String name;

  @NotNull
  private Integer socialNumber;

  @ManyToMany
  private List<Partner> partners;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSocialNumber() {
    return socialNumber;
  }

  public void setSocialNumber(Integer socialNumber) {
    this.socialNumber = socialNumber;
  }

  public List<Partner> getPartners() {
    return partners;
  }

  public void setPartners(List<Partner> partners) {
    this.partners = partners;
  }

}
