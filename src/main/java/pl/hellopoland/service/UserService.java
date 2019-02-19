package pl.hellopoland.service;

import java.util.List;
import java.util.Optional;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserLocation;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.exception.UnauthorizedException;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.util.HelloTicket;

@LocalBean
@Stateless
public class UserService extends ServiceSuperclass {
  @Inject
  private PasswordEncoder passwordEncoder;
  @Inject
  private PartnerService partnerService;

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

  public void changePassword(UserAuthDTO userAuthDTO) {
    if (passwordEncoder.matches(userAuthDTO.oldPassword, getLoggedUser().getPassword())) {
      getLoggedUser().setPassword(passwordEncoder.encode(userAuthDTO.password));
      Portal hpt = getPortal("Hello Ticket Cloud");
      HelloTicket ht = new HelloTicket(hpt.getUrl());
      ht.changePartnerPassword(userAuthDTO, getLoggedPartner().getHptToken());
    } else {
      throw new ConflictingException("Incorrect old password.");
    }
  }

  public List<UserDTO> getUshers() {
    Partner partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    return helloTicket.getUshersForPartner(partner.getHptToken());
  }

}
