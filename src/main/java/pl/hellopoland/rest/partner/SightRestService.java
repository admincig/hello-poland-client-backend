package pl.hellopoland.rest.partner;

import static javax.ws.rs.core.Response.noContent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
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
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightService;
import pl.hellopoland.util.DtoMapper;

@Path("/partner/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SightRestService {

  @Inject
  private SightService sightService;

  @POST
  public Response add(SightDTO sight) throws URISyntaxException {
    return Response.created(new URI("/partner/sights/" + sight.id))
        .entity(DtoMapper.getFullDTO(sightService.create(sight, null))).build();
  }

  @GET
  public Response getList() {
    return Response.ok(new PagedCollection(sightService.getActiveForPartner().stream()
        .map(DtoMapper::getDTO).collect(Collectors.toList()), null)).build();
  }

  @GET
  @Path("/{id}")
  public Response get(@PathParam("id") Long id) {
    return Response.ok(DtoMapper.getFullDTO(sightService.get(id))).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(@PathParam("id") Long id, SightDTO sight) {
    return Response.ok(DtoMapper.getFullDTO(sightService.update(id, sight))).build();
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    sightService.delete(id);
    return noContent().build();
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public Response uploadIcon(@PathParam("id") Long id, byte[] icon) {
    return Response.ok(DtoMapper.getDTO(sightService.uploadMainImageForLoggedUser(id, icon)))
        .build();
  }
}
