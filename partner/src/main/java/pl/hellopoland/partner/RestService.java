package pl.hellopoland.partner;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestService {

  @Inject
  private SecurityContext securityContext;

  @Inject
  private CurrentUser currentUser;

  @GET
  @Path("/helloWorld")
  @RolesAllowed("user")
  public String helloWorld() {
    return "Hello, world!";
  }

  @POST
  @Path("/login")
  public Response login() {
    if (securityContext.getCallerPrincipal() != null) {
      return Response.ok(UserAuthDTO.ofCurrentUser(currentUser)).build();
    }

    return Response.status(Response.Status.UNAUTHORIZED).build();
  }
}
