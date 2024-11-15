package pl.hellopoland.rest.partner;

import pl.hellopoland.security.CurrentUser;

import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static pl.hellopoland.security.UserAuthDTO.ofCurrentUser;

@Path("/partner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"root", "admin", "user"})
public class PartnerAuthenticationRestService {

  @Inject
  private SecurityContext securityContext;

  @Inject
  private CurrentUser currentUser;

  @POST
  @Path("/login")
  public Response login() {
    if (securityContext.getCallerPrincipal() != null) {
      return Response.ok(ofCurrentUser(currentUser)).build();
    }

    return Response.status(Response.Status.UNAUTHORIZED).build();
  }

  @POST
  @Path("/refresh")
  public Response refresh() {
    if (securityContext.getCallerPrincipal() != null) {
      return Response.ok(ofCurrentUser(currentUser)).build();
    }

    return Response.status(UNAUTHORIZED).build();
  }

  @POST
  @Path("/logout")
  public Response logout() {
    return Response.ok().build();
  }
}
