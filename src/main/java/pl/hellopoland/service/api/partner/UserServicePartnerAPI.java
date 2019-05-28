package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.User;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.UserService;

@Stateless
public class UserServicePartnerAPI {

  @Inject
  UserService service;

  @RolesAllowed("partner")
  public UserORO me() {
    User bo = service.me();
    var dto = new UserORO(bo);
    return dto;
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
  public PagedCollection getUshers() {
    return new PagedCollection(service.getUshers(), null);
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
