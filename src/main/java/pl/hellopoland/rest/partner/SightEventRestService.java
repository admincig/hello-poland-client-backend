package pl.hellopoland.rest.partner;

import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
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
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.sight.SightEvent;
import pl.hellopoland.sight.SightEventService;
import pl.hellopoland.util.HplMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Path("/partner/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SightEventRestService {

  @Inject
  SightEventService sightEventService;

  @GET
  @PermitAll
  public PagedCollection getList() {
    return search(new SightsPagedCollectionConfig());
  }

  @POST
  public Response create(pl.hellopoland.dto.SightEvent dto) {
    return Response.ok(HplMapper.getDTO(sightEventService.create(dto, null))).build();
  }

  @PUT
  @Path("/{id}")
  public Response update(@PathParam("id") Long id, pl.hellopoland.dto.SightEvent dto) {
    return Response.ok(HplMapper.getDTO(sightEventService.update(id, dto))).build();
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightsPagedCollectionConfig config) {
    config.onlyCurrentPartner(true);
    config.onlyActive();
    PagedEntityCollection<SightEvent> plist = sightEventService.getList(config);
    return new PagedCollection(
        plist.items.stream().map(HplMapper::getDTO).collect(Collectors.toList()),
        plist.config);
  }

  @GET
  @Path("/{id}")
  public pl.hellopoland.dto.SightEvent get(@PathParam("id") Long id) {
    return HplMapper.getDTO(sightEventService.get(id));
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    sightEventService.delete(id);
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public Response uploadIcon(@PathParam("id") Long id, byte[] icon){
    return Response.ok(HplMapper.getDTO(sightEventService.uploadMainImage(id, icon))).build();
  }
}
