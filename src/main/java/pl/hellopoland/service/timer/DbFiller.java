package pl.hellopoland.service.timer;

import static java.util.Collections.singletonList;
import java.lang.System.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.Portal.Type;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.service.ServiceSuperclass;
import pl.hellopoland.service.SightEventService;

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
    createUsers();



    // createSights();


    logger.log(Logger.Level.INFO, "dbfiller finished");
  }

  @SuppressWarnings("unused")
  private void createUsers() {
    User userHelloPoland = createPartner("Hello Poland",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.");
    User userZoo = createPartner("Zoo",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiJDOTU1NTI0MDk2REU0MjlEQjBGODM1NTA1RUI5MzAxNzkzQzE4NEJBQzM2NTFBNzI2MDFCRDNGMUFEQTkyQzAzIn0.");
    createPartner("Kolejkowo",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiI0MDc5MTkyRkI2NTQyQTYyRjc3QTcwNDZDRDU1QkJGNUM5NDAzNkE0MjRFRDI4RTM0MEYwODNCRDE1MDRFODZBIn0.");
    createPartner("Statek",
        "eyJhbGciOiJub25lIn0.eyJzdWIiOiIyODQyODcyRThEQ0EzMENFNkJBOTk5REMzQjBGODJFNUNFOTNFNzA5RTJEMjlGMEQ4NjFFOTU4QjMxQ0QwQzREIn0.");

  }

  private User createPartner(String partnerName, String token) {
    Partner helloPolandPartner = new Partner();
    helloPolandPartner.setName(partnerName + " Partner");
    helloPolandPartner.setHptToken(token);

    User user = new User();

    UserRole userRole = new UserRole();
    userRole.setRole("partner");
    userRole.setUser(user);

    String email = partnerName.toLowerCase().replaceAll(" ", "") + "@"
        + partnerName.toLowerCase().replaceAll(" ", "") + ".pl";
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(partnerName.toLowerCase().replaceAll(" ", "")));
    user.setRoles(singletonList(userRole));
    user.setPartner(helloPolandPartner);

    em.persist(user);

    return user;
  }


  private void createPortals() {
    createHelloTicketCloud();
  }


  private void createHelloTicketCloud() {
    Portal portal = new Portal();
    portal.setName("Hello Ticket Cloud");
    portal.setUrl("https://hpt.fream.pl/api");
    portal.setType(Type.HELLOTICKET_CLOUD_1);
    em.persist(portal);
  }

  // private void createSights() {
  // createSight();
  // }
  //
  // private void createSight() {
  // var bo = new Sight();
  // bo.setName("Wycieczki Hello Poland we Wrocławiu");
  // bo.setLead("Kupuj taniej, zwiedzaj łatwiej!");
  // bo.setDescription(
  // "Specjalizujemy się w obsłudze zorganizowanych grup turystycznych oraz biznesowych z kraju i
  // zagranicy.");
  //
  // }
  //
  // private void createSightEvent() {
  // var bo = new SightEvent();
  // bo.setName("Wieczorne zwiedzanie Afrykarium");
  // bo.setLead("Kupuj taniej, zwiedzaj łatwiej!");
  // bo.setDescription(
  // "Specjalizujemy się w obsłudze zorganizowanych grup turystycznych oraz biznesowych z kraju i
  // zagranicy.");

  // }

}
