package pl.hellopoland.security.token;

import java.util.Set;
import javax.security.enterprise.credential.Credential;

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
