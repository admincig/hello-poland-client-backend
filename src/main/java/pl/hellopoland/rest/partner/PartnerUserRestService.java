package pl.hellopoland.rest.partner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.partner.UserServicePartnerAPI;

@Path("/partner/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerUserRestService {

  @Inject
  private UserServicePartnerAPI service;

  @GET
  @Path("/me")
  public UserORO me() {
    return service.me();
  }

  @PATCH
  @Path("/me/password")
  public Response changePassword(UserAuthDTO user) {
    if (user.oldPassword.equals(user.password)) {
      return Response.notModified("The new password is equal to the old password.").build();
    }
    service.changePassword(user);
    return Response.ok().build();
  }

}
