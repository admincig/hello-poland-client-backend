package pl.hellopoland.rest.dto;

import pl.hellopoland.user.User;

public class UserORO {

  public String email;
  public String name;
  public String location;
  public String picture;

  public UserORO(User user) {
    this.email = user.getEmail();
    this.name = user.getName();
    this.location = user.getLocation();
    this.picture = user.getPicture();
  }

}
