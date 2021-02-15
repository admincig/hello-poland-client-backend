package pl.hellopoland.security;

import static java.util.Arrays.asList;
import java.util.HashSet;
import java.util.Set;

public class CurrentUser {

  private String email;
  private Set<String> roles = new HashSet<>();
  private String accessToken;
  private String refreshToken;


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

}
