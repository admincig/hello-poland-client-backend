package pl.hellopoland.partner;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.user.User;

@Entity
public class Partner extends ModelSuperclass {

  private static final long serialVersionUID = 6118414827783500940L;

  private String name;

  @OneToMany(mappedBy = "partner")
  private List<User> users;

  @OneToMany(mappedBy = "partner")
  private List<Sight> sight;

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
}