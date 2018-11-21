package pl.hellopoland.service;

import java.io.UnsupportedEncodingException;
import java.lang.System.Logger;
import java.math.BigDecimal;
import java.util.HashMap;
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
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.RoleDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.email.EmailSendingException;
import pl.hellopoland.util.HelloTicket;

@LocalBean
@Stateless
public class HellopolandService extends ServiceSuperclass {
  private static final Logger lOG = System.getLogger("HellopolandService");

  @Inject
  private UserService userService;
  @Inject
  private EmailService emailService;

  final Set<UserRole.Role> excluded_roles = Set.of(UserRole.Role.ROOT, UserRole.Role.ADMIN);

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
    String password = RandomStringUtils.randomAlphanumeric(10);
    userService.create(partner.email, password, null, null, null, partnerBO, UserRole.Role.PARTNER);
    var emailPassword = new HashMap<String, String>();
    emailPassword.put(partner.email, password);

    // 2. creating users of the partner in hpl:
    var usersDTOs = partner.users;
    if (usersDTOs != null && !usersDTOs.isEmpty()) {
      for (UserDTO userDTO : usersDTOs) {
        if (userDTO.roles == null || userDTO.roles.isEmpty()
            || !List.of(UserRole.Role.values()).containsAll(
                getFilteredRolesStreamFromDTO(userDTO.roles).collect(Collectors.toList()))) {
          throw new ConflictingException("Roles are blank or some role is unsupported.");
        }
        if (StringUtils.isBlank(userDTO.email)) {
          throw new ConflictingException("The email cannot be blank.");
        }
        String pass = RandomStringUtils.randomAlphanumeric(10);
        User userBO = userService.create(userDTO.email, pass, userDTO.name, null, null, partnerBO,
            getFilteredRolesStreamFromDTO(userDTO.roles)
                .toArray(size -> new UserRole.Role[userDTO.roles.size()]));
        emailPassword.put(userDTO.email, pass);
        partnerBO.addUser(userBO);
      }
    }

    // 3. creating a partner in hpt:
    Portal hpt = getPortal("Hello Ticket Cloud");
    var ht = new HelloTicket(hpt.getUrl());
    var hptPartner = ht.addPartner(partner);
    partnerBO.setHptToken(hptPartner.token);

    // 4. sending emails to users (with theirs login and password):
    emailPassword.forEach((key, value) -> {
      try {
        emailService.sendEmail(key, "Nowe konto w Hello Poland.",
            "Twój login to " + key + ", hasło to " + value);
      } catch (MessagingException | UnsupportedEncodingException e) {
        lOG.log(System.Logger.Level.ERROR, e.getLocalizedMessage());
        throw new EmailSendingException();
      }
    });

    return partnerBO;
  }

  private Stream<UserRole.Role> getFilteredRolesStreamFromDTO(Set<RoleDTO> roles) {
    return roles.stream().map(r -> UserRole.Role.valueOf(r.name()))
        .filter(r -> !excluded_roles.contains(r));
  }

}
