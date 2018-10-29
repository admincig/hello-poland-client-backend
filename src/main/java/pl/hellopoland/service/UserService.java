package pl.hellopoland.service;

import java.util.Optional;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserLocation;
import pl.hellopoland.exception.UnauthorizedException;

@LocalBean
@Stateless
public class UserService extends ServiceSuperclass {

  public User me() {
    return Optional.ofNullable(getLoggedUser()).orElseThrow(UnauthorizedException::new);
  }

  public User getOrCreateSocialMedia(User user) {
    String email = user.getEmail();
    String name = user.getName();
    String picture = user.getPicture();
    UserLocation location = user.getLocation();
    try {
      return findOneByEmail(email);
    } catch (NoResultException e) {
      return create(email, null, name, picture, location);
    }
  }

  private User create(String email, String password, String name, String picture,
      UserLocation location) {
    User bo = new User("user");
    bo.setEmail(email);
    bo.setName(name);
    bo.setPassword(password);
    bo.setPicture(picture);
    bo.setLocation(location);

    em.persist(bo);
    return bo;
  }

  public User findOneByEmail(String email) {
    return em.createQuery("from User where email=:email", User.class).setParameter("email", email)
        .getSingleResult();
  }

  public Optional<User> findByEmail(String email) {
    return em.createQuery("from User where email=:email", User.class).setParameter("email", email)
        .getResultStream().findFirst();
  }
}
