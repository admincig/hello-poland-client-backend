package pl.hellopoland.user;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import pl.hellopoland.ServiceSuperclass;

@LocalBean
@Stateless
public class UserService extends ServiceSuperclass {

  @RolesAllowed("user")
  public User me() {
    return findByEmail(ctx.getCallerPrincipal().getName());
  }

  @PermitAll
  public User getOrCreateSocialMedia(User user) {
    String email = user.getEmail();
    String name = user.getName();
    String picture = user.getPicture();
    UserLocation location = user.getLocation();
    try {
      return findByEmail(email);
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

  public User findByEmail(String email) {
    return em.createQuery("from User where email=:email", User.class).setParameter("email", email)
        .getSingleResult();
  }
}
