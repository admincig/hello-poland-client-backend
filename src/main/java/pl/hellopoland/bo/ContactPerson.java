package pl.hellopoland.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class ContactPerson extends ModelSuperclass {
  private static final long serialVersionUID = -1768101360763400631L;

  @NotBlank
  private String name;

  @NotBlank
  private String email;

  @NotBlank
  private String phone;

  @OneToMany(mappedBy = "contactPerson")
  private List<Partner> partners;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public List<Partner> getPartners() {
    return partners;
  }

  public void setPartners(List<Partner> partners) {
    this.partners = partners;
  }

}
