package pl.hellopoland.service.api.market;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.User;
import pl.hellopoland.dto.UserInfoDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.UserService;

@Stateless
public class UserServiceMarketAPI {

  @Inject
  UserService service;

  @RolesAllowed("user")
  public UserORO me() {
    User bo = service.me();
    var dto = new UserORO(bo);
    return dto;
  }

  @PermitAll
  public UserORO register(UserInfoDTO userDTO) {
    if (!userDTO.password.equals(userDTO.passwordConfirmation)) {
      throw new ConflictingException("Passwords are not the same");
    }

    User newUser = service.create(userDTO.email, userDTO.password, userDTO.tosAgreement);
    var dto = new UserORO(newUser);
    return dto;
  }

  @RolesAllowed("user")
  public UserORO update(UserInfoDTO userDTO) {
    User newUser = service.update(
        userDTO.email,
        userDTO.firstName,
        userDTO.lastName,
        userDTO.phone,
        userDTO.street,
        userDTO.zipCode,
        userDTO.city,
        userDTO.country);
    var dto = new UserORO(newUser);
    return dto;
  }
}
