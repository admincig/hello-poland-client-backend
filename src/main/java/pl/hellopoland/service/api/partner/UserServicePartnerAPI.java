package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.User;
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
  public PagedCollection getUshers() {
    return new PagedCollection(service.getUshers(), null);
  }
}
