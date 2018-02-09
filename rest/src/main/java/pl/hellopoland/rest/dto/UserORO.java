package pl.hellopoland.rest.dto;

import pl.hellopoland.user.User;
import pl.hellopoland.util.NameAndAddressSplitter;

public class UserORO {

  public String email;
  public String name;
  public String firstName;
  public String lastName;
  public UserLocationRO location;
  public String picture;

  public UserORO(User user) {
    this.email = user.getEmail();
    this.name = user.getName();
    this.firstName = NameAndAddressSplitter.getFirstName(user.getName());
    this.lastName = NameAndAddressSplitter.getLastName(user.getName());
    this.location = new UserLocationRO(user.getLocation());
    this.picture = user.getPicture();
  }

}
