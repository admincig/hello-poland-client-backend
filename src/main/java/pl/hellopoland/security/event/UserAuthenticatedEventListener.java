package pl.hellopoland.security.event;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import pl.hellopoland.security.dto.CurrentUser;


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
