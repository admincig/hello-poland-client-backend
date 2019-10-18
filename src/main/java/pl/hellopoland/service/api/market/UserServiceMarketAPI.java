package pl.hellopoland.service.api.market;

import java.lang.System.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserDetails;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserInfoDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.UserService;

@Stateless
public class UserServiceMarketAPI {
  private static Logger staticLogger = System.getLogger(UserServiceMarketAPI.class.getName());

  @Inject
  UserService service;

  @RolesAllowed("user")
  public UserORO me() {
    User bo = service.me();
    var dto = new UserORO(bo);
    return dto;
  }

  @PermitAll
  public void register(UserInfoDTO userDTO) {
    validatePassword(userDTO.password, userDTO.passwordConfirmation);
    try {
      service.create(userDTO.email, userDTO.password, userDTO.tosAgreement);
    } catch (EJBTransactionRolledbackException e) {
      staticLogger.log(Logger.Level.WARNING, "User with email already exists:" + userDTO.email);
    }
  }

  @RolesAllowed("user")
  public UserORO update(UserInfoDTO userDTO) {
    UserDetails details = new UserDetails();
    details.setStreet(userDTO.street);
    details.setCity(userDTO.city);
    details.setCountry(userDTO.country);
    details.setFirstName(userDTO.firstName);
    details.setLastName(userDTO.lastName);
    details.setPhone(userDTO.phone);
    details.setZipCode(userDTO.zipCode);

    User newUser = service.updateUserDetailsForLoggedUser(details);
    var dto = new UserORO(newUser);
    return dto;
  }

  @RolesAllowed("user")
  public void updatePassword(UserAuthDTO userAuthDTO) {
    validatePassword(userAuthDTO.password, userAuthDTO.passwordConfirmation);
    service.changePasswordForLoggedUser(userAuthDTO);
  }

  private void validatePassword(String password, String passwordConfirmation) {
    if (password == null || passwordConfirmation == null
        || !password.equals(passwordConfirmation)) {
      throw new ConflictingException("Passwords are not the same");
    }
  }

}
