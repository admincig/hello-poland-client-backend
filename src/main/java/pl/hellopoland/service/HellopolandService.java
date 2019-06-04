package pl.hellopoland.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.config.PartnerCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.RoleDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.email.EmailSendingRollbackException;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

@LocalBean
@Stateless
public class HellopolandService extends ServiceSuperclass {
  @Inject
  private UserService userService;
  @Inject
  private EmailService emailService;

  final Set<UserRole.Role> excluded_roles =
      Set.of(UserRole.Role.ROOT, UserRole.Role.ADMIN, UserRole.Role.PARTNER);

  public Partner addPartner(PartnerDTO partner) {
    if (StringUtils.isBlank(partner.email)) {
      throw new ConflictingException("The email cannot be blank.");
    }
    if (StringUtils.isBlank(partner.name)) {
      throw new ConflictingException("The partner name cannot be blank.");
    }
    if (partner.commission == null) {
      throw new ConflictingException("The partner commission cannot be blank.");
    }
    if (partner.commission.compareTo(BigDecimal.ZERO) == -1
        || partner.commission.compareTo(new BigDecimal("100")) == 1) {
      throw new ConflictingException("The partner commission is out of range: 0 - 100.");
    }

    // 1. creating a partner and the user in hpl:
    var partnerBO = new Partner();
    partnerBO.setName(partner.name);
    partnerBO.setP24Id(partner.p24MerchantId);
    partnerBO.setCommission(partner.commission);
    partnerBO.setHptToken("temporaryToken");
    partnerBO.setEmail(partner.email);
    partnerBO.setAffiliateCode(partner.affiliateCode);
    String password = RandomStringUtils.randomAlphanumeric(10);
    userService.create(partner.email, password, null, null, null, partnerBO, UserRole.Role.PARTNER,
        UserRole.Role.USHER);
    partner.password = password;
    var emailPassword = new HashMap<String, String>();
    emailPassword.put(partner.email, password);

    // 2. creating users (excluded ushers) of the partner in hpl:
    var usersDTOs = partner.users;
    if (usersDTOs != null && !usersDTOs.isEmpty()) {
      for (UserDTO userDTO : usersDTOs) {
        if (StringUtils.isBlank(userDTO.email)) {
          throw new ConflictingException("The email cannot be blank.");
        }
        if (userDTO.roles == null || !areRolesSupported(userDTO.roles)) {
          throw new ConflictingException("Roles are blank or some role is unsupported.");
        }
        Role[] userRoles = getFilteredRolesFromDTO(userDTO.roles);
        if (userRoles.length > 0) {
          String pass = RandomStringUtils.randomAlphanumeric(10);
          User userBO = userService.create(userDTO.email, pass, userDTO.name, null, null, partnerBO,
              userRoles);
          emailPassword.put(userDTO.email, pass);
          partnerBO.addUser(userBO);
        }
      }
    }

    // 3. creating a partner in hpt:
    try {
      Portal hpt = getPortal("Hello Ticket Cloud");
      var ht = new HelloTicket(hpt.getUrl());
      var hptPartner = ht.addPartner(partner, getLoggedUser().getHptToken());
      partnerBO.setHptToken(hptPartner.token);
    } catch (Exception e) {
      throw new ConflictingException("Nie udało się stworzyć partnera w zewnętrznym systemie", e);
    }

    // 4. sending emails to users (with theirs login and password):
    emailPassword.forEach((key, value) -> {
      try {
        emailService.sendEmail(key, "Nowe konto w Hello Poland.",
            "Twój login to " + key + ", hasło to " + value);
      } catch (MessagingException | UnsupportedEncodingException e) {
        logger.log(System.Logger.Level.ERROR, e.getLocalizedMessage());
        throw new EmailSendingRollbackException();
      }
    });

    return partnerBO;
  }

  private boolean isAtLeastOneUsher(List<UserDTO> usersDTOs) {
    return usersDTOs.stream().anyMatch(user -> user.roles.contains(RoleDTO.USHER));
  }

  private boolean areRolesSupported(Set<RoleDTO> roles) {
    var supported = new HashSet<Role>(Arrays.asList(UserRole.Role.values()));
    supported.removeAll(excluded_roles);
    try {
      return supported.containsAll(
          roles.stream().map(r -> UserRole.Role.valueOf(r.name())).collect(Collectors.toSet()));
    } catch (Exception e) {
      return false;
    }
  }

  private Role[] getFilteredRolesFromDTO(Set<RoleDTO> roles) {
    Stream<UserRole.Role> stream = roles.stream().map(r -> UserRole.Role.valueOf(r.name()))
        .filter(r -> !excluded_roles.contains(r) && !r.equals(UserRole.Role.USHER));
    return stream.toArray(UserRole.Role[]::new);
  }

  public PagedEntityCollection<Partner> getList(PartnerCollectionConfig config) {
    List<Partner> partners = getQuery(config).getResultList();
    return null;gdhfgf
  }

}
