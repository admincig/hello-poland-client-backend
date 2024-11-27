package pl.hellopoland.bo;

import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.security.password.PasswordEncoder;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "users_email_unique"))
public class User extends ModelSuperclass implements HptSubject {

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
  private String password;
  // TODO delete name after rework
  private String name;

  private String picture;
  @Embedded
  private UserDetails details;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = EAGER)
  private List<UserRole> roles;
  @ManyToOne(cascade = PERSIST, fetch = LAZY)
  private Partner partner;
  private String hptToken;
  private boolean deleted;
  @ManyToMany(mappedBy = "users")
  private Set<Sight> sights;
  @ManyToMany(mappedBy = "users")
  private Set<SightEvent> sightEvents;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void changePassword(String password) {
    if (password == null || password.length() < 3) {
      throw new ConflictingException("New password cannot be empty or have less than 3 characters");
    }
    this.password = new PasswordEncoder().encode(password);
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public UserDetails getDetails() {
    return details;
  }

  public void setDetails(UserDetails details) {
    this.details = details;
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

  @Override
  public String getHptToken() {
    return hptToken;
  }

  public void setHptToken(String hptToken) {
    this.hptToken = hptToken;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    String toString = "Email: " + this.email + "\n";
    UserDetails d = this.getDetails();
    if (d != null) {
      if (d.getFirstName() != null) {
        toString += "Imię: " + d.getFirstName() + "\n";
      }
      if (d.getLastName() != null) {
        toString += "Nazwisko: " + d.getLastName();
      }
    }
    return toString;
  }

  public boolean hasSight(Sight sight) {
    return sights.stream().anyMatch(s -> s.getId().equals(sight.getId()));
  }
}
