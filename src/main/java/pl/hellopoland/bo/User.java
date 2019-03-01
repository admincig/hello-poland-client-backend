package pl.hellopoland.bo;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "users_email_unique"))
public class User extends ModelSuperclass {

  private static final long serialVersionUID = -2816139938781126241L;

  public User() {}

  public User(UserRole.Role... roles) {
    for (var role : roles) {
      this.addRole(role);
    }
  }

  @NotNull
  @Email
  private String email;
  private String name;
  private String password;
  private String picture;
  @Embedded
  private UserLocation location;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = EAGER)
  private List<UserRole> roles;
  @ManyToOne(cascade = PERSIST)
  private Partner partner;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public UserLocation getLocation() {
    return location;
  }

  public void setLocation(UserLocation location) {
    this.location = location;
  }

  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }

  private void addRole(UserRole.Role role) {
    UserRole ur = new UserRole();
    ur.setUser(this);
    ur.setRole(role);
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(ur);
  }

  public boolean hasRole(UserRole.Role role) {
    return roles.stream().anyMatch(ur -> ur.getRole().equals(role));
  }

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

}
