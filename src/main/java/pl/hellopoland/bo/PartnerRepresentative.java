package pl.hellopoland.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class PartnerRepresentative extends ModelSuperclass {
  private static final long serialVersionUID = -6996932813336717031L;

  @NotBlank
  private String name;

  @NotNull
  private Long socialNumber;

  @ManyToMany(mappedBy = "representatives")
  private List<Partner> partners;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSocialNumber() {
    return socialNumber;
  }

  public void setSocialNumber(Long socialNumber) {
    this.socialNumber = socialNumber;
  }

  public List<Partner> getPartners() {
    return partners;
  }

  public void setPartners(List<Partner> partners) {
    this.partners = partners;
  }

}
