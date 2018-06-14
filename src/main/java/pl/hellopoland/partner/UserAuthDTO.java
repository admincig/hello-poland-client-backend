package pl.hellopoland.partner;

import java.io.Serializable;

public class UserAuthDTO implements Serializable {

  private static final long serialVersionUID = 1790587694120511896L;

  public String login;
  public String password;
  public String accessToken;
  public String refreshToken;

  public static UserAuthDTO ofCurrentUser(CurrentUser currentUser) {
    UserAuthDTO dto = new UserAuthDTO();
    dto.login = currentUser.getEmail();
    dto.accessToken = currentUser.getAccessToken();
    dto.refreshToken = currentUser.getRefreshToken();
    return dto;
  }
}
