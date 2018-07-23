package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.User;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.TicketService;
import pl.hellopoland.service.UserService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class UserServicePartnerAPI {

  @Inject
  UserService service;

  @RolesAllowed("partner")
  public UserORO me(){
    User bo = service.me();
    var dto = new UserORO(bo);
    return dto;
  }
}
