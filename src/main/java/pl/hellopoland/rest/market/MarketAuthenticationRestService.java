package pl.hellopoland.rest.market;

import jakarta.annotation.security.PermitAll;
import jakarta.persistence.PersistenceContext;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserToken;
import pl.hellopoland.rest.dto.PasswordResetConfirmDTO;
import pl.hellopoland.rest.dto.PasswordResetRequestDTO;
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
import pl.hellopoland.service.UserService;

import java.util.Optional;

import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static pl.hellopoland.security.UserAuthDTO.ofCurrentUser;

@Path("/market")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@DeclareRoles({"root", "admin", "user"})
public class MarketAuthenticationRestService {

  @Inject
  private SecurityContext securityContext;

  @Inject
  private CurrentUser currentUser;

  @Inject
  private UserService userService;

  @PersistenceContext
  private jakarta.persistence.EntityManager em;


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

  @POST
  @Path("/password-reset")
  @PermitAll
  public Response requestPasswordReset(PasswordResetRequestDTO dto) {
    Optional<User> userOpt = Optional.empty();

    if (dto != null && dto.email != null) {
      userOpt = userService.findUndeletedByEmail(dto.email)
          .filter(userService::isActiveMarketUser);
    }

    if (userOpt.isPresent()) {
      String token = java.util.UUID.randomUUID().toString()
          + java.util.UUID.randomUUID().toString();
      userService.createPasswordResetToken(userOpt.get(), token);
    }

    // Always return 200 to avoid revealing whether the address belongs to a portal account.
    return Response.ok().build();
  }

    @POST
    @Path("/password-reset/confirm")
    @PermitAll
    public Response confirmPasswordReset(PasswordResetConfirmDTO dto) {

        if (dto == null || dto.token == null || dto.newPassword == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        UserToken resetToken = userService.findValidPasswordResetToken(dto.token);

        if (resetToken == null || !userService.isActiveMarketUser(resetToken.getUser())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean success = userService.confirmPasswordReset(dto.token, dto.newPassword);

        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }
    @POST
    @Path("/activate-account")
    @PermitAll
    public Response activateAccount(PasswordResetConfirmDTO dto) {

        if (dto == null || dto.token == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean success = userService.confirmEmailVerification(dto.token);

        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

}
