package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserORO {

  public String email;
  public Long id;
  public String name;
  public String firstName;
  public String lastName;
  public UserLocationRO location;
  public String picture;
  public UserDetailsRO details;
  public Set<String> roles;

  public UserORO(User user) {
    this.id = user.getId();
    this.email = user.getEmail();

    if (user.getDetails() != null) {
      this.details = new UserDetailsRO(user.getDetails());
      this.location = new UserLocationRO(user.getDetails());
      this.firstName = details.firstName;
      this.lastName = details.lastName;
      this.name = Stream.of(this.firstName, this.lastName)
          .filter(part -> part != null && !part.isBlank())
          .collect(Collectors.joining(" "));
    }
    if (this.name == null || this.name.isBlank()) {
      this.name = user.getName() != null && !user.getName().isBlank() ? user.getName() : user.getEmail();
    }

    this.roles = user.getRoles().stream()
              .map(UserRole::getRole)
              .map(Enum::name)
              .collect(Collectors.toSet());

    this.picture = user.getPicture();
  }

}
