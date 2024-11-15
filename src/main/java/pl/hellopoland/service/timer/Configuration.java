package pl.hellopoland.service.timer;

import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.util.FacebookAPIConnector;
import pl.hellopoland.util.GoogleAPIConnector;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.inject.Produces;

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

  public static void main(String... strings) {
    var p = new PasswordEncoder().encode("hp-bileter");
    System.out.println(p);
  }
}
