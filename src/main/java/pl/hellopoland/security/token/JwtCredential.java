package pl.hellopoland.security.token;

import jakarta.security.enterprise.credential.Credential;
import java.util.Set;

public class JwtCredential implements Credential {

  public JwtCredential(String principal, Set<String> authorities) {
    this.principal = principal;
    this.authorities = authorities;
  }

  private final String principal;
  private final Set<String> authorities;

  public String getPrincipal() {
    return principal;
  }

  public Set<String> getAuthorities() {
    return authorities;
  }

}
