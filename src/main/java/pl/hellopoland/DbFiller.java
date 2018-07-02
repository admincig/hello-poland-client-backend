package pl.hellopoland;

import static java.util.Collections.singletonList;

import java.lang.System.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.partner.Partner;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.sight.Portal;
import pl.hellopoland.sight.Portal.Type;
import pl.hellopoland.sight.SightEventService;
import pl.hellopoland.user.User;
import pl.hellopoland.user.UserRole;

@Startup
@Singleton
@DependsOn({"Configuration"})
public class DbFiller extends ServiceSuperclass {

  @Inject
  SightEventService sService;

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
    createPartner("Hello Poland",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.");
    createPartner("Zoo",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiJDOTU1NTI0MDk2REU0MjlEQjBGODM1NTA1RUI5MzAxNzkzQzE4NEJBQzM2NTFBNzI2MDFCRDNGMUFEQTkyQzAzIn0.");
    createPartner("Kolejkowo",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiI0MDc5MTkyRkI2NTQyQTYyRjc3QTcwNDZDRDU1QkJGNUM5NDAzNkE0MjRFRDI4RTM0MEYwODNCRDE1MDRFODZBIn0.");
    createPartner("Statek",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiIyODQyODcyRThEQ0EzMENFNkJBOTk5REMzQjBGODJFNUNFOTNFNzA5RTJEMjlGMEQ4NjFFOTU4QjMxQ0QwQzREIn0.");
  }

  private void createPartner(String partnerName, String token) {
    Partner helloPolandPartner = new Partner();
    helloPolandPartner.setName(partnerName + " Partner");
    helloPolandPartner.setHptToken(token);

    User user = new User();

    UserRole userRole = new UserRole();
    userRole.setRole("partner");
    userRole.setUser(user);

    String email = partnerName.toLowerCase().replaceAll(" ", "") + "@" + partnerName.toLowerCase()
        .replaceAll(" ", "") + ".pl";
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(partnerName.toLowerCase().replaceAll(" ", "")));
    user.setRoles(singletonList(userRole));
    user.setPartner(helloPolandPartner);

    em.persist(user);
  }


  private void runImporter() {
    sService.runImporter();
  }


  private void createPortals() {
    createTestWooHelloPoland();
    createHelloTicketCloud();
  }


  private void createHelloTicketCloud() {
    Portal portal = new Portal();
    portal.setName("Hello Ticket Cloud");
    portal.setUrl("https://hpt.fream.pl/api");
    portal.setType(Type.HELLOTICKET_CLOUD_1);
    em.persist(portal);
  }


  private void createTestWooHelloPoland() {
    Portal portal = new Portal();
    portal.setName("[TEST] Wycieczki Hello Poland");
    portal.setUrl("http://woo.hello-poland.pl");
    portal.setKey("ck_5233b79180ff8b7bef81b28fe7222b2eb2b37ebe");
    portal.setSecret("cs_2c96f574d729e8bde7b71d96007c172bc12244d9");
    portal.setType(Type.WOOCOMMERCE);
    em.persist(portal);
  }

}
