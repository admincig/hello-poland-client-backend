package pl.hellopoland.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class UserRole extends ModelSuperclass {

  private static final long serialVersionUID = -2777254091194478668L;

  public enum Role {
    ROOT, ADMIN, HELPDESK_PARTNER_MANAGER, HELPDESK_CONTENT_MANAGER, HELPDESK_SUPPORT, PARTNER,
    PARTNER_ADMIN, PARTNER_SALESMAN, SALESMAN, USHER, USER;

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
