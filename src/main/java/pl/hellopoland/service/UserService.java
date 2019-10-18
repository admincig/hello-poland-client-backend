package pl.hellopoland.service;

import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserDetails;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.exception.UnauthorizedException;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.NameAndAddressSplitter;

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
    String picture = user.getPicture();
    UserDetails details = user.getDetails();
    try {
      User bo = findOneByEmail(email);
      bo.setPicture(picture);
      if (!bo.hasRole(Role.USER)) {
        createUserRole(bo, Role.USER);
      }
      return bo;
    } catch (NoResultException e) {
      return create(email, null, picture, details);
    }
  }

  private User create(String email, String password, String picture,
      UserDetails details) {
    User bo = new User(Role.USER);
    bo.setEmail(email.toLowerCase());
    bo.setPassword(password);
    bo.setPicture(picture);
    bo.setDetails(details);

    em.persist(bo);
    return bo;
  }

  public User create(String email, String decodedPassword, boolean tosAgreement) {
    User bo = new User(Role.USER);
    if (email == null) {
      throw new ConflictingException("Cannot create user with empty email");
    }
    bo.setEmail(email.toLowerCase());
    bo.setPassword(passwordEncoder.encode(decodedPassword));

    UserDetails details = new UserDetails();
    details.setTosAgreement(tosAgreement);
    bo.setDetails(details);

    em.persist(bo);
    return bo;
  }

  public User create(String email, String decodedPassword, String name, String picture,
      Partner partner, UserRole.Role... roles) {
    User bo = new User(roles);
    if (email == null) {
      throw new ConflictingException("Cannot create user with empty email");
    }
    bo.setEmail(email.toLowerCase());
    bo.setPassword(passwordEncoder.encode(decodedPassword));
    bo.setPicture(picture);
    bo.setPartner(partner);

    bo.setDetails(new UserDetails(NameAndAddressSplitter.getFirstName(name),
        NameAndAddressSplitter.getLastName(name)));
    em.persist(bo);
    return bo;
  }

  public User updateUserDetailsForLoggedUser(UserDetails newDetails) {
    User user = getLoggedUser();
    UserDetails oldDetails = user.getDetails();
    if (oldDetails == null && newDetails == null) {
      oldDetails = new UserDetails();
    }
    oldDetails.update(newDetails);
    em.merge(user);
    return user;
  }

  public User findOneByEmail(String email) {
    return em.createQuery("from User where lower(email) = :email", User.class)
        .setParameter("email", email.toLowerCase()).getSingleResult();
  }

  public Optional<User> findByEmail(String email) {
    return em.createQuery("from User where lower(email) = :email", User.class)
        .setParameter("email", email.toLowerCase()).getResultStream().findFirst();
  }

  public Optional<User> findByEmailWithNullPartner(String email) {
    return em.createQuery("from User where lower(email) = :email and partner = null", User.class)
        .setParameter("email", email.toLowerCase()).getResultStream().findFirst();
  }

  public void changePasswordForLoggedPartner(UserAuthDTO userAuthDTO) {
    if (passwordEncoder.matches(userAuthDTO.oldPassword, getLoggedUser().getPassword())) {
      getLoggedUser().setPassword(passwordEncoder.encode(userAuthDTO.password));
      Portal hpt = getPortal("Hello Ticket Cloud");
      HelloTicket ht = new HelloTicket(hpt.getUrl());
      ht.changePartnerPassword(userAuthDTO, getLoggedPartner().getHptToken());
    } else {
      throw new ConflictingException("Incorrect old password.");
    }
  }

  public void changePasswordForUsher(long usherId, UserAuthDTO userAuthDTO) {
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());
    ht.changeUsherPassword(usherId, userAuthDTO, getLoggedPartner().getHptToken());
  }

  public void changePasswordForLoggedUser(UserAuthDTO userAuthDTO) {
    if (passwordEncoder.matches(userAuthDTO.oldPassword, getLoggedUser().getPassword())) {
      getLoggedUser().setPassword(passwordEncoder.encode(userAuthDTO.password));
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

  public UserDTO getUsher(long usherId) {
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());
    return ht.getUsherForPartner(usherId, getLoggedPartner().getHptToken());
  }

  public UserDTO updateUsher(UserDTO usher) {
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());
    return ht.updateUsherForPartner(usher, getLoggedPartner().getHptToken());
  }

  public UserDTO createUsherForLoggedPartner(UserDTO usherDTO) {
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());
    return ht.createUsherForLoggedPartner(usherDTO, getLoggedPartner().getHptToken());
  }

  public void attachToPartner(User user, Partner partner) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setPartner(partner);
    createUserRole(user, Role.USHER);
    createUserRole(user, Role.PARTNER);
  }

  private void createUserRole(User user, Role role) {
    UserRole ur = new UserRole();
    ur.setUser(user);
    ur.setRole(role);
    em.persist(ur);
    if (user.getRoles() == null) {
      user.setRoles(new ArrayList<>());
    }
    user.getRoles().add(ur);
  }

  public Set<String> getFlatRoles(String email) {
    return findByEmail(email).get().getRoles().stream().map(UserRole::getRole).map(Role::toString)
        .collect(Collectors.toSet());
  }

  // TODO delete this
  public void globalRework() {
    List<User> all = getAll();
    for (User user : all) {
      String oldName = user.getName();

      try {
        if (user.getDetails() == null) {
          user.setDetails(new UserDetails());
        }
        user.getDetails().setFirstName(NameAndAddressSplitter.getFirstName(oldName));
        user.getDetails().setLastName(NameAndAddressSplitter.getLastName(oldName));
      } catch (ConflictingException e) {
        logger.log(Level.ERROR,
            "Did not updated first and last name for user[id:" + user.getId() + "]");
      }

      em.merge(user);
    }

  }

  public List<User> getAll() {
    return em.createQuery("from User", User.class)
        .getResultList();
  }

}
