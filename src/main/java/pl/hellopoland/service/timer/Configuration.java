package pl.hellopoland.service.timer;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
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

  public static void main(String... strings) {
    var p = new PasswordEncoder().encode("hellopoland");
    System.out.println(p);
  }
}
