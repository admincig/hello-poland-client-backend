package pl.hellopoland.service.timer;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.util.FacebookAPIConnector;
import pl.hellopoland.util.GoogleAPIConnector;

@Startup
@Singleton
@DataSourceDefinition(
    name = "java:global/jdbc/hellopolandDS",
    className = "org.postgresql.xa.PGXADataSource",
    serverName = "localhost",
    portNumber = 5432,
    databaseName = "hellopoland",
    user = "hellopoland",
    password = "hellopoland")
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
