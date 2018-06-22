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

  @OneToMany
  private List<User> users;

  @OneToMany
  private List<Sight> sightEvents;

}