package pl.hellopoland;

import static java.util.Collections.singletonList;

import java.lang.System.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.sight.Portal;
import pl.hellopoland.sight.Portal.Type;
import pl.hellopoland.sight.SightService;
import pl.hellopoland.user.User;
import pl.hellopoland.user.UserRole;

@Startup
@Singleton
@DependsOn({"Configuration"})
public class DbFiller extends ServiceSuperclass {

  @Inject
  SightService sService;

  @Inject
  private PasswordEncoder passwordEncoder;

  @PostConstruct
  public void fillDb() {
    String hibernateStrategy = properties.get("hibernate.hbm2ddl.auto").toString();
    if (!hibernateStrategy.startsWith("create")) {
      logger.log(Logger.Level.INFO,
          "omitting dbfiller because hibernate.hbm2ddl.auto isnt set to create");
      return;
    }

    createPortals();
//    runImporter();
    createUsers();
    logger.log(Logger.Level.INFO, "dbfiller finished");
  }

  private void createUsers() {
    User user = new User();

    UserRole userRole = new UserRole();
    userRole.setRole("partner");
    userRole.setUser(user);

    user.setEmail("partner@partner.pl");
    user.setPassword(passwordEncoder.encode("partner"));
    user.setRoles(singletonList(userRole));

    em.persist(user);
  }


  private void runImporter() {
    sService.runImporter();
  }


  private void createPortals() {
    createTestWooHelloPoland();
  }


  private void createTestWooHelloPoland() {
    Portal portal = new Portal();
    portal.setName("[TEST] Wycieczki Hello Poland");
    portal.setUrl("http://woo.hello-poland.pl");
    portal.setKey("ck_5233b79180ff8b7bef81b28fe7222b2eb2b37ebe");
    portal.setSecret("cs_2c96f574d729e8bde7b71d96007c172bc12244d9");
    portal.setType(Type.WOOCOMMERCE);
    em.persist(portal);

    portal = new Portal();
    portal.setName("Hello Ticket Cloud");
    portal.setUrl("https://hpt.fream.pl");
    portal.setType(Type.HELLOTICKET_CLOUD_1);
    em.persist(portal);
  }

}
