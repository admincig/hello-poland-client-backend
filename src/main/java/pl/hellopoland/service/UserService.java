package pl.hellopoland.service;

import java.util.Optional;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserLocation;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.exception.UnauthorizedException;
import pl.hellopoland.security.password.PasswordEncoder;

@LocalBean
@Stateless
public class UserService extends ServiceSuperclass {
  @Inject
  private PasswordEncoder passwordEncoder;

  public User me() {
    return Optional.ofNullable(getLoggedUser()).orElseThrow(UnauthorizedException::new);
  }

  public User getOrCreateSocialMedia(User user) {
    String email = user.getEmail();
    String name = user.getName();
    String picture = user.getPicture();
    UserLocation location = user.getLocation();
    try {
      User bo = findOneByEmail(email);
      bo.setPicture(picture);
      return bo;
    } catch (NoResultException e) {
      return create(email, null, name, picture, location);
    }
  }

  private User create(String email, String password, String name, String picture,
      UserLocation location) {
    User bo = new User(Role.USER);
    bo.setEmail(email);
    bo.setName(name);
    bo.setPassword(password);
    bo.setPicture(picture);
    bo.setLocation(location);

    em.persist(bo);
    return bo;
  }

  public User create(String email, String decodedPassword, String name, String picture,
      UserLocation location, Partner partner, UserRole.Role... roles) {
    User bo = new User(roles);
    bo.setEmail(email);
    bo.setName(name);
    bo.setPassword(passwordEncoder.encode(decodedPassword));
    bo.setPicture(picture);
    bo.setLocation(location);
    bo.setPartner(partner);
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
