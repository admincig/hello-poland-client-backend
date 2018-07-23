package pl.hellopoland.service.timer;

import java.io.IOException;
import java.io.InputStream;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.util.FacebookAPIConnector;
import pl.hellopoland.util.GoogleAPIConnector;

@Startup
@Singleton
public class Configuration {

  public Configuration() {
    java.util.Properties systemProps = System.getProperties();
    try (InputStream customProps = Configuration.class.getResourceAsStream("/config.properties")) {
      systemProps.load(customProps);
    } catch (IOException e) {
    }
  }

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
