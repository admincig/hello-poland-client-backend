package pl.hellopoland.rest.partner;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.security.CurrentUser;
import pl.hellopoland.service.UserService;

@Path("/partner/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestService {

  @Inject
  private UserService userService;

  @GET
  @Path("/me")
  public UserORO me() {
    return new UserORO(userService.me());
  }

}
