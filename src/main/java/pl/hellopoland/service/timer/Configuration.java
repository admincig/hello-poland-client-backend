package pl.hellopoland.service.timer;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.inject.Produces;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.util.FacebookAPIConnector;
import pl.hellopoland.util.GoogleAPIConnector;

@Startup
@Singleton
public class Configuration {

  @Produces
  public PasswordEncoder passwordEncoder() {
    return new PasswordEncoder();
  }

  @Produces
  public FacebookAPIConnector facebookAPIConnector() {
    return new FacebookAPIConnector();
  }

  @Produces
  public GoogleAPIConnector googleAPIConnector() {
    return new GoogleAPIConnector();
  }

}
