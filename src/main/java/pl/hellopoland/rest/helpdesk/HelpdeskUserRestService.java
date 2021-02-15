package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.helpdesk.UserServiceHelpdeskAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
  @Path("/{id}/password")
  public Response resetPassword(@PathParam("id") Long id,
      UserDTO dto) {
    service.updatePassword(id, dto.password);
    return Response.ok().build();
  }

}
