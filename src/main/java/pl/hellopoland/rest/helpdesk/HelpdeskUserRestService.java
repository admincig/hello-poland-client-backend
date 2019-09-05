package pl.hellopoland.rest.helpdesk;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.helpdesk.UserServiceHelpdeskAPI;

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

}
