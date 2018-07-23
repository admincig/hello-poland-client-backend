package pl.hellopoland.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;


@RequestScoped
public class UserAuthenticatedEventListener {

  private CurrentUser currentUser;

  @Produces
  public CurrentUser getUserInfo() {
    return this.currentUser;
  }

  public void handleAuthenticationEvent(@Observes CurrentUser authenticatedUser) {
    this.currentUser = authenticatedUser;
  }

}
