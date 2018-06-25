package pl.hellopoland.rest;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static pl.hellopoland.security.dto.UserAuthDTO.ofCurrentUser;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
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
import pl.hellopoland.security.dto.CurrentUser;

@RequestScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"root", "admin", "user"})
public class AuthenticationRestService {

  @Inject
  private SecurityContext securityContext;

  @Inject
  private CurrentUser currentUser;

  @GET
  @Path("/secured/helloWorld")
  @DenyAll
  public String securedHelloWorld() {
    return "Hello, world!";
  }

  @GET
  @Path("/helloWorld")
  @PermitAll
  public String helloWorld() {
    return "Hello, world!";
  }

  @POST
  @Path("login")
  public Response login() {
    if (securityContext.getCallerPrincipal() != null) {
      return Response.ok(ofCurrentUser(currentUser)).build();
    }

    return Response.status(Response.Status.UNAUTHORIZED).build();
  }

  @POST
  @Path("refresh")
  public Response refresh() {
    if (securityContext.getCallerPrincipal() != null) {
      return Response.ok(ofCurrentUser(currentUser))
          .build();
    }

    return Response.status(UNAUTHORIZED).build();
  }

  @POST
  @Path("logout")
  public Response logout() {
    return Response.ok().build();
  }
}
