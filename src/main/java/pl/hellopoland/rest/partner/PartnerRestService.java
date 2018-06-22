package pl.hellopoland.rest.partner;

import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.user.UserService;

@Path("/partner")
@RequestScoped
public class PartnerRestService {

  Logger logger = Logger.getLogger(PartnerRestService.class.getName());

  @Inject
  private UserService userService;

  @Inject
  private CurrentUser currentUser;

  @GET
  @Path("/me")
  @RolesAllowed("user")
  public UserORO me() {
    return new UserORO(userService.me(currentUser));
  }
}
