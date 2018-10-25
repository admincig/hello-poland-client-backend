package pl.hellopoland.bo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Partner extends ModelSuperclass {

  private static final long serialVersionUID = 6118414827783500940L;

  // @NotNull
  // @Column(nullable = false)
  private Integer p24Id;

  @NotNull
  @Column(nullable = false)
  private String name;

  @NotNull
  @Column(nullable = false)
  private String hptToken;

  @OneToMany(mappedBy = "partner")
  private List<User> users;

  @OneToMany(mappedBy = "partner")
  private List<Sight> sight;

  @OneToMany(mappedBy = "partner")
  private List<SightEvent> sightEvents;

  @OneToMany(mappedBy = "partner")
  private List<Agreement> agreements;

  public Integer getP24Id() {
    return p24Id;
  }

  public void setP24Id(Integer p24Id) {
    this.p24Id = p24Id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public List<Sight> getSight() {
    return sight;
  }

  public void setSight(List<Sight> sight) {
    this.sight = sight;
  }

  public String getHptToken() {
    return hptToken;
  }

  public void setHptToken(String hptToken) {
    this.hptToken = hptToken;
  }

  public List<Agreement> getAgreements() {
    return agreements;
  }

  public void setAgreements(List<Agreement> agreements) {
    this.agreements = agreements;
  }

  public void addUser(User user) {
    if (this.users == null) {
      this.users = new ArrayList<>();
    }
    this.users.add(user);
  }

}
