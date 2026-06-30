package pl.hellopoland.rest.partner;

import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.partner.UserServicePartnerAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

  @GET
  public Response getPartnerPanelUsers() {
    return Response.ok(service.getPartnerPanelUsers()).build();
  }

  @POST
  public Response createPartnerPanelUser(UserDTO userDTO) {
    return Response.ok(service.createPartnerPanelUser(userDTO)).build();
  }

  @GET
  @Path("/{id}")
  public Response getPartnerPanelUser(@PathParam("id") long userId) {
    return Response.ok(service.getPartnerPanelUser(userId)).build();
  }

  @PATCH
  @Path("/{id}")
  public Response updatePartnerPanelUser(@PathParam("id") long userId, UserDTO userDTO) {
    return Response.ok(service.updatePartnerPanelUser(userId, userDTO)).build();
  }

  @PATCH
  @Path("/{id}/password")
  public Response changePartnerPanelUserPassword(@PathParam("id") long userId, UserAuthDTO userDTO) {
    service.changePasswordForPartnerPanelUser(userId, userDTO);
    return Response.ok().build();
  }

  @DELETE
  @Path("/{id}")
  public Response deletePartnerPanelUser(@PathParam("id") long userId) {
    service.deletePartnerPanelUser(userId);
    return Response.noContent().build();
  }

}
