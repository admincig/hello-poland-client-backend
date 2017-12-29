package pl.fream.bootstrap.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.fream.bootstrap.model.User;

@LocalBean
@Stateless
public class Service extends ServiceSuperclass {

  public String greetings() {
    log.info("Greetings");
    saveTestUserInDatabase();
    return "EJB Service is working";
  }

  private void saveTestUserInDatabase() {
    User user = new User();
    user.setEmail("greetings@fream.pl");
    em.persist(user);
  }
}
