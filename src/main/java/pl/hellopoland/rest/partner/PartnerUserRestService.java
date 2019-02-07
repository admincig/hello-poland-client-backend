package pl.hellopoland.rest.partner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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

  @PUT
  @Path("/password")
  public Response changePassword(UserAuthDTO user) {
    service.changePassword(user);
    return Response.ok().build();
  }

}
