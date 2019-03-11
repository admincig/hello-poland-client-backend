package pl.hellopoland.rest.partner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.UserServicePartnerAPI;

@Path("/partner")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerRestService {

  @Inject
  private UserServicePartnerAPI service;

  @GET
  @Path("/ushers")
  public PagedCollection getUshers() {
    return service.getUshers();
  }

  @GET
  @Path("/ushers/{id}")
  public Response getUsher(@PathParam("id") long usherId) {
    return Response.ok(service.getUsher(usherId)).build();
  }

  @PATCH
  @Path("/ushers/{id}")
  public Response updateUsher(@PathParam("id") long usherId, UserDTO usher) {
    usher.id = usherId;
    return Response.ok(service.updateUsher(usher)).build();
  }

  @PATCH
  @Path("/ushers/{id}/password")
  public Response changePasswordForUsher(@PathParam("id") long usherId, UserAuthDTO usherDTO) {
    service.changePasswordForUsher(usherId, usherDTO);
    return Response.ok().build();
  }

}
