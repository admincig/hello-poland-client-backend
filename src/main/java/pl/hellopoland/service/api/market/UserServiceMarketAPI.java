package pl.hellopoland.service.api.market;

import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserDetails;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserInfoDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.UserService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.lang.System.Logger;

@Stateless
public class UserServiceMarketAPI {
  private static Logger staticLogger = System.getLogger(UserServiceMarketAPI.class.getName());

  @Inject
  UserService service;
  @Inject
  MailingListServiceMarketAPI mailingListAPI;

  @RolesAllowed({"user", "root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public UserORO me() {
    User bo = service.me();
    var dto = new UserORO(bo);
    return dto;
  }

  @PermitAll
  public void register(UserInfoDTO userDTO) {
    if (userDTO == null || userDTO.email == null || userDTO.email.trim().isEmpty()) {
      throw new ConflictingException("Adres e-mail jest wymagany.");
    }
    validatePassword(userDTO.password, userDTO.passwordConfirmation);
    if (userDTO.tosAgreement == null) {
      staticLogger.log(Logger.Level.WARNING, "tosAgreement is required");
      throw new ConflictingException("tosAgreement is required");
    }
    userDTO.email = userDTO.email.trim().toLowerCase();
    User user = service.registerMarketUser(
        userDTO.email,
        userDTO.password,
        userDTO.tosAgreement);

    if (userDTO.addToMailingList) {
      mailingListAPI.addToMailingList(user.getEmail());
    }
  }

  @RolesAllowed("user")
  public UserORO updateUserDetails(UserInfoDTO userDTO) {
    UserDetails details = new UserDetails();
    details.setStreet(userDTO.street);
    details.setCity(userDTO.city);
    details.setCountry(userDTO.country);
    details.setFirstName(userDTO.firstName);
    details.setLastName(userDTO.lastName);
    details.setPhone(userDTO.phone);
    details.setZipCode(userDTO.zipCode);

    User user = service.updateUserDetailsForLoggedUser(details);
    var dto = new UserORO(user);
    return dto;
  }

  // TODO przygotowanie pod przyszłe zmiany - osobna metoda do updatu agreementów
  @RolesAllowed("user")
  public UserORO updateAgreements(UserInfoDTO userDTO) {
    UserDetails details = new UserDetails();
    details.setTosAgreement(userDTO.tosAgreement);
    User user = service.updateAgreementsForLoggedUser(details);
    var dto = new UserORO(user);
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
