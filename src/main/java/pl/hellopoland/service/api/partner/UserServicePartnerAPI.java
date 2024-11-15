package pl.hellopoland.service.api.partner;

import pl.hellopoland.bo.User;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class UserServicePartnerAPI {

  @Inject
  UserService service;
  @Inject
  PartnerService partnerService;
  @Inject
  TranslationService transService;

  @RolesAllowed("partner")
  public UserORO me() {
    User bo = service.me();
    return new UserORO(bo);
  }

  @RolesAllowed("partner")
  public void changePasswordForLoggedPartner(UserAuthDTO userDTO) {
    service.changePasswordForLoggedPartner(userDTO);
  }

  @RolesAllowed("partner")
  public void changePasswordForUsher(long usherId, UserAuthDTO usherDTO) {
    service.changePasswordForUsher(usherId, usherDTO);
  }

  @RolesAllowed("partner")
  public UserDTO createUsher(UserDTO usherDTO) {
    return service.createUsherForLoggedPartner(usherDTO);
  }

  @RolesAllowed("partner")
  public PagedCollection<UserDTO> getUshers() {
    return new PagedCollection<>(service.getUshers(), null);
  }

  @RolesAllowed("partner")
  public UserDTO getUsher(long usherId) {
    return service.getUsher(usherId);
  }

  @RolesAllowed("partner")
  public UserDTO updateUsher(UserDTO usher) {
    return service.updateUsher(usher);
  }

}
