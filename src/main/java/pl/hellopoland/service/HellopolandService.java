package pl.hellopoland.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import pl.hellopoland.util.HelloTicket;

@LocalBean
@Stateless
public class HellopolandService extends ServiceSuperclass {
  @Inject
  private UserService userService;

  public Partner addPartner(PartnerDTO partner) {
    if (StringUtils.isBlank(partner.email)) {
      throw new ConflictingException(""); // TODO: add message
    }
    if (StringUtils.isBlank(partner.name)) {
      throw new ConflictingException(""); // TODO: add message
    }

    // 1. tworzenie partnera i odpowiedniego uzytkownika w hpl:
    var partnerBO = new Partner();
    partnerBO.setName(partner.name);
    partnerBO.setP24Id(partner.p24MerchantId);
    partnerBO.setHptToken("temporaryToken");
    String password = RandomStringUtils.randomAlphanumeric(10);
    userService.create(partner.email, password, null, null, null, partnerBO, UserRole.Role.PARTNER);

    // 2. tworzenie uzytkownikow dla danego partnera:
    var emailPassword = new HashMap<String, String>();
    var usersDTOs = partner.users;
    if (usersDTOs != null && !usersDTOs.isEmpty()) {
      for (UserDTO userDTO : usersDTOs) {
        if (userDTO.roles == null || userDTO.roles.isEmpty() || !List.of(UserRole.Role.values())
            .containsAll(getRolesStreamFromDTO(userDTO.roles).collect(Collectors.toList()))) {
          throw new ConflictingException(""); // TODO: add message
        }
        if (StringUtils.isBlank(userDTO.email)) {
          throw new ConflictingException(""); // TODO: add message
        }
        String pass = RandomStringUtils.randomAlphanumeric(10);
        User userBO = userService.create(userDTO.email, pass, userDTO.name, null, null, partnerBO,
            getRolesStreamFromDTO(userDTO.roles)
                .toArray(size -> new UserRole.Role[userDTO.roles.size()]));
        emailPassword.put(userDTO.email, pass);
        partnerBO.addUser(userBO);
      }
    }

    // 3. utworzenie partnera w hpt:
    Portal hpt = getPortal("Hello Ticket Cloud");
    var ht = new HelloTicket(hpt.getUrl());
    var hptPartner = ht.addPartner(partner);
    partnerBO.setHptToken(hptPartner.token);

    // 4. przeslanie hasel uzytkownikow i loginu do partnera:

    return partnerBO;
  }

  private Stream<UserRole.Role> getRolesStreamFromDTO(Set<RoleDTO> roles) {
    return roles.stream().map(r -> UserRole.Role.valueOf(r.name()));
  }

}
