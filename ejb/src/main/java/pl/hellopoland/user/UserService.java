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
  public User getOrCreateSocialMedia(User user) {
    String email = user.getEmail();
    String name = user.getName();
    String picture = user.getPicture();
    String location = user.getLocation();
    try {
      return findByEmail(email);
    } catch (NoResultException e) {
      return create(email, null, name, picture, location);
    }
  }

  private User create(String email, String password, String name, String picture, String location) {
    User bo = new User();
    bo.setEmail(email);
    bo.setName(name);
    bo.setPassword(password);
    bo.setPicture(picture);
    bo.setLocation(location);
    em.persist(bo);
    return bo;
  }

  @PermitAll
  public User findByEmail(String email) {
    return em.createQuery("from User where email=:email", User.class).setParameter("email", email)
        .getSingleResult();
  }
}
