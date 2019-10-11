package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.User;

public class UserORO {

  public String email;
  public String name;
  public String firstName;
  public String lastName;
  public UserLocationRO location;
  public String picture;

  public UserORO(User user) {
    this.email = user.getEmail();

    if (user.getDetails() != null) {
      this.location = new UserLocationRO(user.getDetails());
      this.firstName = user.getDetails().getFirstName();
      this.lastName = user.getDetails().getLastName();
      this.name = firstName + " " + lastName;
    }

    this.picture = user.getPicture();
  }

}
