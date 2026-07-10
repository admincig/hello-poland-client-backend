package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.helpdesk.UserServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/helpdesk/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskUserRestService {

  @Inject
  UserServiceHelpdeskAPI service;

  @GET
  @Path("/me")
  public UserORO me() {
    return service.me();
  }

  @PATCH
  @Path("/me/password")
  public Response changeOwnPassword(UserAuthDTO dto) {
    service.changeOwnPassword(dto);
    return Response.ok().build();
  }

  @PUT
  @Path("/me/avatar")
  @Consumes({"image/jpeg", "image/jpg", "image/webp", "image/png"})
  public UserORO updateOwnAvatar(byte[] bytes, @HeaderParam("Content-Type") String contentType) {
    String extension = "jpeg";
    if ("image/png".equals(contentType)) {
      extension = "png";
    } else if ("image/webp".equals(contentType)) {
      extension = "webp";
    }
    return service.updateOwnAvatar(bytes, extension);
  }

  @GET
  public Response listUsers() {
    return Response.ok(service.getHelpdeskUsers()).build();
  }

  @POST
  public Response createUser(UserDTO dto) {
    return Response.ok(service.createHelpdeskUser(dto)).build();
  }

  @GET
  @Path("/{id}")
  public Response getUser(@PathParam("id") Long id) {
    return Response.ok(service.getHelpdeskUser(id)).build();
  }

  @PUT
  @Path("/{id}")
  public Response updateUser(@PathParam("id") Long id, UserDTO dto) {
    return Response.ok(service.updateHelpdeskUser(id, dto)).build();
  }

  @PATCH
  @Path("/{id}/password")
  public Response resetPassword(@PathParam("id") Long id,
      UserDTO dto) {
    service.updatePassword(id, dto.password);
    return Response.ok().build();
  }

  @PATCH
  @Path("/{id}/blocked")
  public Response setBlocked(@PathParam("id") Long id, UserDTO dto) {
    boolean blocked = Boolean.TRUE.equals(dto.blocked);
    return Response.ok(service.setHelpdeskUserBlocked(id, blocked)).build();
  }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        service.deleteUser(id);
        return Response.noContent().build();
    }
}
