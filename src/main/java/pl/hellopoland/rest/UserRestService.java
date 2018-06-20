package pl.hellopoland.rest;

import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.rest.dto.LoginIRO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.security.dto.UserAuthDTO;
import pl.hellopoland.user.UserService;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestService {

  Logger logger = Logger.getLogger(UserRestService.class.getName());
  @Context
  HttpServletRequest req;

  @Inject
  UserService userService;

  @Inject
  private CurrentUser currentUser;

  @GET
  @Path("/users/me")
  @RolesAllowed("user")
  public UserORO me() {
    return new UserORO(userService.me());
  }

  @POST
  @Path("/login")
  public Response loginBySocialMedia(UserAuthDTO user) {
    return Response.ok(currentUser).build();
  }

  @POST
  @Path("/login/socialMedia")
  public Response loginBySocialMedia(LoginIRO iro) {
    return Response.ok(currentUser).build();
  }

  @GET
  @Path("/logout")
  public void logout() {
    try {
      req.logout();
    } catch (ServletException e) {
    }
    req.getSession().invalidate();
  }

  private boolean login(String string, String password) {
    try {
      req.login("", password);
      return true;
    } catch (ServletException e) {
      logger.warning("Failed to log in: " + e.getMessage());
      return false;
    }
  }
}
