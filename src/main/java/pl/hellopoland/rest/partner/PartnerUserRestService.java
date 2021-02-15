package pl.hellopoland.rest.partner;

import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.partner.UserServicePartnerAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
  public Response changePassword(UserAuthDTO userDTO) {
    if (userDTO.oldPassword.equals(userDTO.password)) {
      return Response.notModified("The new password is equal to the old password.").build();
    }
    service.changePasswordForLoggedPartner(userDTO);
    return Response.ok().build();
  }

}
