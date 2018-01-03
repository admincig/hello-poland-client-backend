package pl.hellopoland.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class UserRole extends ModelSuperclass {
  private static final long serialVersionUID = -2777254091194478668L;

  @ManyToOne(optional = false)
  private User user;

  @NotNull
  private String role;


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
