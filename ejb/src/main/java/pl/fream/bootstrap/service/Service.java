package pl.fream.bootstrap.service;

import java.util.UUID;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.fream.bootstrap.model.User;
import pl.fream.bootstrap.model.UserRole;
import pl.fream.bootstrap.service.mdb.HelloWorldMDB;

@LocalBean
@Stateless
public class Service extends ServiceSuperclass {

  @PermitAll
  public String greetings() {
    logger.info("Greetings");
    saveTestUserInDatabase();
    sendMessage(HelloWorldMDB.QUEUE, "hello world");
    return "EJB Service is working";
  }

  @RolesAllowed("user")
  public void secured() {
    ;
  }

  private void saveTestUserInDatabase() {
    User user = new User();
    user.setEmail(UUID.randomUUID().toString().substring(0, 8) + "@fream.pl");
    em.persist(user);
    logger.info("saved user in db: " + user.getEmail());
    UserRole ur = new UserRole();
    ur.setUser(user);
    ur.setRole("user");
    em.persist(ur);
  }
}
