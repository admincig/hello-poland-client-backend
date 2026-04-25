package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;

import java.util.Set;
import java.util.stream.Collectors;

public class UserORO {

  public String email;
  public String name;
  public String firstName;
  public String lastName;
  public UserLocationRO location;
  public String picture;
  public UserDetailsRO details;
  public Set<String> roles;

  public UserORO(User user) {
    this.email = user.getEmail();

    if (user.getDetails() != null) {
      this.details = new UserDetailsRO(user.getDetails());
      this.location = new UserLocationRO(user.getDetails());
      this.firstName = details.firstName;
      this.lastName = details.lastName;
      this.name = this.firstName + " " + this.lastName;
    }

    this.roles = user.getRoles().stream()
              .map(UserRole::getRole)
              .map(Enum::name)
              .collect(Collectors.toSet());

    this.picture = user.getPicture();
  }

}
