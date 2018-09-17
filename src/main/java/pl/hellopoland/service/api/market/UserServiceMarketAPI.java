package pl.hellopoland.service.api.market;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.User;
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
}
