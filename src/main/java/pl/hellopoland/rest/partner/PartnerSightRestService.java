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
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightService;
import pl.hellopoland.service.api.partner.SightServicePartnerAPI;
import pl.hellopoland.util.DtoMapper;

@Path("/partner/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerSightRestService {

  @Inject
  private SightServicePartnerAPI service;

  @POST
  public pl.hellopoland.dto.Sight add(pl.hellopoland.dto.Sight dto) {
    return service.create(dto);
  }

  @GET
  public PagedCollection getList() {
    return service.getList();
  }

  @GET
  @Path("/{id}")
  public pl.hellopoland.dto.Sight get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @PUT
  @Path("/{id}")
  public pl.hellopoland.dto.Sight update(@PathParam("id") Long id, pl.hellopoland.dto.Sight dto) {
    dto.id = id;
    return service.update(dto);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public pl.hellopoland.dto.Sight uploadMainImage(@PathParam("id") Long id, byte[] icon){
    return service.uploadMainImage(id, icon);
  }
  
}
