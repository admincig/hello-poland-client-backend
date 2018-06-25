package pl.hellopoland.rest.partner;

import static javax.ws.rs.core.Response.noContent;

import java.net.URI;
import java.net.URISyntaxException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.dto.Sight;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.sight.SightService;

@Path("/partner/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SightRestService {

  @Inject
  private SightService sightService;

  @Inject
  private CurrentUser currentUser;

  @POST
  public Response add(Sight sight) throws URISyntaxException {
    return Response.created(new URI("/partner/sights/" + sight.id))
        .entity(sightService.add(sight, currentUser))
        .build();
  }

  @GET
  public Response get() {
    return Response.ok(sightService.getAllForPartner(currentUser)).build();
  }

  @GET
  @Path("/{id}")
  public Response get(@PathParam("id") Long id) {
    return Response.ok(sightService.get(id)).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(@PathParam("id") Long id, Sight sight) {
    return Response.ok(sightService.update(id, sight, currentUser)).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    sightService.delete(id);

    return noContent().build();
  }
}
