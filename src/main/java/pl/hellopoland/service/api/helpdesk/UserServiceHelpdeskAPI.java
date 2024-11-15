package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.User;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class UserServiceHelpdeskAPI {

  @Inject
  UserService service;

  @RolesAllowed("admin")
  public UserORO me() {
    User bo = service.me();
    var dto = new UserORO(bo);
    return dto;
  }

  @RolesAllowed("admin")
  public void updatePassword(Long id, String newPassword) {
    User bo = service.get(id);
    service.updatePasswordForUser(bo, newPassword);

  }
}
