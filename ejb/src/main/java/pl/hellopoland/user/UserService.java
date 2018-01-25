package pl.hellopoland.user;

import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import pl.hellopoland.ServiceSuperclass;

@LocalBean
@Stateless
public class UserService extends ServiceSuperclass {

  @PermitAll
  public User getOrCreateFacebook(User user) {
    String email = user.getEmail();
    String name = user.getName();
    try {
      return findByEmail(email);
    } catch (NoResultException e) {
      return create(email, name, null);
    }
  }

  private User create(String email, String name, String password) {
    User bo = new User();
    bo.setEmail(email);
    bo.setName(name);
    bo.setPassword(password);
    em.persist(bo);
    return bo;
  }

  @PermitAll
  public User findByEmail(String email) {
    return em.createQuery("from User where email=:email", User.class).setParameter("email", email)
        .getSingleResult();
  }
}
