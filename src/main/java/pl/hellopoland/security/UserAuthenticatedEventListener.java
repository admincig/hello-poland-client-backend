package pl.hellopoland.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;


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
