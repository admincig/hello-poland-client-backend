package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class UserRole extends ModelSuperclass {

  private static final long serialVersionUID = -2777254091194478668L;

  public enum Role {
    ROOT, ADMIN, PARTNER, SALESMAN, USHER, USER;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }

  @ManyToOne(optional = false)
  private User user;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

}
