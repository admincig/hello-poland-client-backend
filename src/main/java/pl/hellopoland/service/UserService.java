package pl.hellopoland.service;

import org.apache.commons.lang3.RandomStringUtils;
import pl.hellopoland.bo.*;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.exception.UnauthorizedException;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.NameAndAddressSplitter;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import java.lang.System.Logger.Level;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

@LocalBean
@Stateless
public class UserService extends ServiceSuperclass {
  private static final String USHER_EMAIL_MATCHES_PARTNER_EMAIL_MESSAGE =
      "Podaj inny adres e-mail niż ten, którym logujesz się do profilu Partnera.";
  private static final String USHER_EMAIL_ALREADY_USED_AS_LOGIN_MESSAGE =
      "Podany adres e-mail jest już używany.";

  @Inject
  private PasswordEncoder passwordEncoder;
  @Inject
  private PartnerService partnerService;
  @Inject
  private EmailService emailService;
  @Inject
  private OrderService orderService;

  public User me() {
    return Optional.ofNullable(getLoggedUser()).orElseThrow(UnauthorizedException::new);
  }

  public User getOrCreateSocialMedia(User user) {
    String email = user.getEmail();
    String picture = user.getPicture();
    UserDetails details = user.getDetails();
    try {
      User bo = findOneUndeletedByEmail(email);
      bo.setPicture(picture);
      if (!bo.hasRole(Role.USER)) {
        createUserRole(bo, Role.USER);
      }
      return bo;
    } catch (NoResultException e) {
      return create(email, RandomStringUtils.randomAlphanumeric(10), picture, details);
    }
  }

  private User create(String email, String password, String picture,
      UserDetails details) {
    User bo = new User(Role.USER);
    bo.setEmail(email.toLowerCase());
    bo.changePassword(password);
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
    bo.changePassword(decodedPassword);
    bo.setEmailVerified(false);

    UserDetails details = new UserDetails();
    details.setTosAgreement(tosAgreement);
    bo.setDetails(details);

    em.persist(bo);
    createEmailVerificationToken(bo);
    return bo;
  }

  public User create(String email, String decodedPassword, String name, String picture,
      Partner partner, UserRole.Role... roles) {
    User bo = new User(roles);
    if (email == null) {
      throw new ConflictingException("Cannot create user with empty email");
    }
    bo.setEmail(email.toLowerCase());
    bo.changePassword(decodedPassword);
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
    if (oldDetails == null) {
      oldDetails = new UserDetails();
    }
    oldDetails.update(newDetails);
    em.merge(user);
    return user;
  }

  // TODO osobna metoda dla zgód, na przyszły refactor
  public User updateAgreementsForLoggedUser(UserDetails userAgreements) {
    User user = getLoggedUser();
    if (user.getDetails() == null) {
      user.setDetails(new UserDetails());
    }
    UserDetails oldDetails = user.getDetails();
    oldDetails.updateAgreements(userAgreements);
    if (!oldDetails.hasAllRequiredAgreements()) {
      deleteUser(user);
    }
    return user;
  }

  private void deleteUser(User user) {
    String hplMail = properties.getProperty("mail.hellopoland.biuro");
    String hplMailContent = "Dane usuniętego użytkownika: " + user.toString();
    String userEmail = user.getEmail();

    user.setDeleted(true);
    user.setPicture(null);
    orderService.anonymizeOrdersForUser(user);
    anonymizeUser(user);
    try {
      emailService.sendEmail(new Email(hplMail, "Usunięto konto użytkownika", hplMailContent));
      emailService.sendEmail(new Email(userEmail, "Usunięto konto w Hello Poland", "Usunięto Twoje konto w systemie Hello Poland. Usunięto Twoje dane osobowe w związku z cofnięciem zgody na warunki zawarte w naszym regulaminie oraz polityce prywatności."));
    } catch (Exception e) {
      logger.log(System.Logger.Level.ERROR, e.getLocalizedMessage());
      throw new InternalServerErrorException(
          "Something went wrong while deleting user [id:" + user.getId() + "]");
    }
  }

  private void anonymizeUser(User user) {
    user.setEmail("anon+" + user.getId().toString() + "@hello-poland.pl");
    UserDetails details = user.getDetails();
    if (details != null) {
      details.setFirstName("anon");
      details.setLastName("anon");
      details.setStreet("anon");
      details.setPhone("anon");
    }
  }

  public User get(Long id) {
    return em.createQuery("from User where id=:id and deleted=false", User.class)
        .setParameter("id", id).getResultStream().findFirst()
        .orElseThrow(() -> new NotFoundException());
  }

  public User findOneUndeletedByEmail(String email) {
    return em.createQuery("from User where lower(email) = :email and deleted=false", User.class)
        .setParameter("email", email.toLowerCase()).getSingleResult();
  }

  public Optional<User> findUndeletedByEmail(String email) {
    return em.createQuery("from User u left join fetch u.partner fp where lower(u.email) = :email and u.deleted=false", User.class)
        .setParameter("email", email.toLowerCase()).getResultStream().findFirst();
  }

  public Optional<User> findUndeletedByEmailWithNullPartner(String email) {
    return em
        .createQuery("from User where lower(email) = :email and partner = null and deleted=false",
            User.class)
        .setParameter("email", email.toLowerCase()).getResultStream().findFirst();
  }

  public void changePasswordForLoggedPartner(UserAuthDTO userAuthDTO) {
    if (passwordEncoder.matches(userAuthDTO.oldPassword, getLoggedUser().getPassword())) {
      getLoggedUser().changePassword(userAuthDTO.password);
      Portal hpt = getPortal("Hello Ticket Cloud");
      HelloTicket ht = new HelloTicket(hpt.getUrl());
      ht.changePartnerCredentials(userAuthDTO, getLoggedPartner().getHptToken());
    } else {
      throw new ConflictingException("Incorrect old password.");
    }
  }

  public void updatePasswordForUser(User user, String password) {
    user.changePassword(password);
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());

    var dto = new UserAuthDTO();
    dto.password = password;
    if (user.getPartner() != null) {
      ht.changePartnerCredentials(dto, user.getPartner().getHptToken());
    }
  }

  public void changePasswordForUsher(long usherId, UserAuthDTO userAuthDTO) {
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());
    ht.changeUsherPassword(usherId, userAuthDTO, getLoggedPartner().getHptToken());
  }

  public void changePasswordForLoggedUser(UserAuthDTO userAuthDTO) {
    if (passwordEncoder.matches(userAuthDTO.oldPassword, getLoggedUser().getPassword())) {
      getLoggedUser().changePassword(userAuthDTO.password);
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
    validateUsherEmailForPartnerAndHelpdeskLogins(usherDTO);
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket ht = new HelloTicket(hpt.getUrl());
    return ht.createUsherForLoggedPartner(usherDTO, getLoggedPartner().getHptToken());
  }

  private void validateUsherEmailForPartnerAndHelpdeskLogins(UserDTO usherDTO) {
    String usherEmail = StringUtils.trim(usherDTO.email);
    if (StringUtils.equalsIgnoreCase(usherEmail, getLoggedUser().getEmail())) {
      throw new ConflictingException(USHER_EMAIL_MATCHES_PARTNER_EMAIL_MESSAGE);
    }
    if (StringUtils.isBlank(usherEmail)) {
      return;
    }

    findUndeletedByEmail(usherEmail)
        .filter(this::isPartnerOrHelpdeskLogin)
        .ifPresent(user -> {
          throw new ConflictingException(USHER_EMAIL_ALREADY_USED_AS_LOGIN_MESSAGE);
        });
    usherDTO.email = usherEmail;
  }

  private boolean isPartnerOrHelpdeskLogin(User user) {
    return hasAnyRole(user, Role.PARTNER, Role.ADMIN, Role.SALESMAN);
  }

  private boolean hasAnyRole(User user, Role... roles) {
    Set<Role> expectedRoles = Set.of(roles);
    return Optional.ofNullable(user.getRoles()).orElse(List.of()).stream()
        .map(UserRole::getRole)
        .anyMatch(expectedRoles::contains);
  }

  public void attachToPartner(User user, Partner partner) {
    user.changePassword(user.getPassword());
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
    return findUndeletedByEmail(email).get().getRoles().stream().map(UserRole::getRole)
        .map(Role::toString)
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

  public User findByPartnerAndRole(Partner partner, Role role) {
    return em.createQuery("select u from User u join u.roles roles where roles.role = :role and u.partner = :partner", User.class)
        .setParameter("role", role)
        .setParameter("partner", partner)
        .getSingleResult();
  }

    public void createPasswordResetToken(User user, String token) {
        int ttl = Integer.parseInt(
                properties.getProperty("password.reset.token.ttl.minutes", "30")
        );

        UserToken ut = new UserToken();
        ut.setToken(token);
        ut.setUser(user);
        ut.setType(UserToken.Type.PASSWORD_RESET);
        ut.setExpiryDate(LocalDateTime.now().plusMinutes(ttl));
        ut.setUsed(false);

        em.persist(ut);

        String portalUrl = properties.getProperty(
                "portal.url",
                "https://api.hello-poland.pl"
        );

        String resetUrl = portalUrl + "/reset-password?token=" + token;

        String subject = properties.getProperty(
                "password.reset.mail.subject",
                "Reset hasła - Hello Poland"
        );

        String bodyTemplate = properties.getProperty("password.reset.mail.body", "");

        String body = bodyTemplate
                .replace("{resetUrl}", resetUrl)
                .replace("{ttlMinutes}", String.valueOf(ttl));
        body = body.replace("\\n", System.lineSeparator());

        try {
            emailService.sendEmail(
                    new Email(
                            user.getEmail(),
                            subject,
                            body
                    )
            );
        } catch (Exception e) {
            logger.log(System.Logger.Level.ERROR,
                    "Failed to send password reset email for user[id:" + user.getId() + "]",
                    e);
        }
    }

    public UserToken findValidPasswordResetToken(String token) {

        return em.createQuery("""
            from UserToken t
            where t.token = :token
              and t.type = :type
              and t.used = false
              and t.expiryDate > :now
            """, UserToken.class)
                .setParameter("token", token)
                .setParameter("type", UserToken.Type.PASSWORD_RESET)
                .setParameter("now", LocalDateTime.now())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public boolean confirmPasswordReset(String token, String newPassword) {

        UserToken ut = findValidPasswordResetToken(token);

        if (ut == null) {
            return false;
        }
        User user = ut.getUser();
        // zmiana hasla
        user.changePassword(newPassword);
        // oznaczenie tokena jako uzyty
        ut.setUsed(true);

        return true;
    }

    public void createEmailVerificationToken(User user) {

        int ttl = Integer.parseInt(
                properties.getProperty("email.verification.token.ttl.minutes", "60")
        );

        String token = java.util.UUID.randomUUID().toString()
                + java.util.UUID.randomUUID().toString();

        UserToken ut = new UserToken();
        ut.setToken(token);
        ut.setUser(user);
        ut.setType(UserToken.Type.EMAIL_VERIFICATION);
        ut.setExpiryDate(LocalDateTime.now().plusMinutes(ttl));
        ut.setUsed(false);

        em.persist(ut);

        String portalUrl = properties.getProperty(
                "portal.url",
                "https://api.hello-poland.pl"
        );

        String activationUrl = portalUrl + "/activate-account?token=" + token;

        String subject = properties.getProperty(
                "email.verification.mail.subject",
                "Aktywacja konta - Hello Poland"
        );

        String bodyTemplate =
                properties.getProperty("email.verification.mail.body", "");

        String body = bodyTemplate
                .replace("{activationUrl}", activationUrl)
                .replace("{ttlMinutes}", String.valueOf(ttl))
                .replace("\\n", System.lineSeparator());

        try {
            emailService.sendEmail(
                    new Email(user.getEmail(), subject, body)
            );
        } catch (Exception e) {
            logger.log(System.Logger.Level.ERROR,
                    "Failed to send activation email for user[id:" + user.getId() + "]",
                    e);
        }
    }

    public boolean confirmEmailVerification(String token) {

        UserToken ut = em.createQuery("""
        from UserToken t
        where t.token = :token
          and t.type = :type
          and t.used = false
          and t.expiryDate > :now
        """, UserToken.class)
                .setParameter("token", token)
                .setParameter("type", UserToken.Type.EMAIL_VERIFICATION)
                .setParameter("now", LocalDateTime.now())
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (ut == null) {
            return false;
        }

        User user = ut.getUser();
        user.setEmailVerified(true);
        ut.setUsed(true);

        return true;
    }

    public void deletePartnerUserFromHelpdesk(User user) {
        if (user == null || user.isDeleted()) {
            throw new NotFoundException();
        }

        if (user.getPartner() == null) {
            throw new ConflictingException("User is not assigned to partner.");
        }

        if (user.hasRole(Role.ADMIN) || user.hasRole(Role.ROOT)) {
            throw new ConflictingException("Cannot delete admin user.");
        }

        user.setDeleted(true);
        //user.setEmail("deleted+" + user.getId() + "+" + System.currentTimeMillis() + "@hello-poland.pl");
        //user.changePassword(RandomStringUtils.randomAlphanumeric(32));

        em.merge(user);
    }

    public void deleteUsher(long usherId) {
        Portal hpt = getPortal("Hello Ticket Cloud");
        HelloTicket ht = new HelloTicket(hpt.getUrl());
        ht.deleteUsherForPartner(usherId, getLoggedPartner().getHptToken());
    }

}
