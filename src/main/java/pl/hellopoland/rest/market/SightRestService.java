package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.sight.SightService;

@Path("/market/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SightRestService {

  @Inject
  private SightService sightService;

  @GET
  public Response get() {
    return Response
        .ok(new PagedCollection(sightService.get(), null))
        .build();
  }


}
