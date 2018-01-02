package pl.fream.bootstrap.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "users_email_unique"))
public class User extends ModelSuperclass {
  private static final long serialVersionUID = -2816139938781126241L;

  @NotNull
  private String email;


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
