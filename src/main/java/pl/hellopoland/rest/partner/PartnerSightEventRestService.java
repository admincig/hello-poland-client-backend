package pl.hellopoland.rest.partner;

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
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.service.api.partner.SightEventServicePartnerAPI;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Path("/partner/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerSightEventRestService {

  @Inject
  SightEventServicePartnerAPI service;

  @GET
  public PagedCollection getList() {
    return search(new SightEventPagedCollectionConfig());
  }

  @POST
  public pl.hellopoland.dto.SightEvent create(pl.hellopoland.dto.SightEvent dto) {
    return service.create(dto);
  }

  @PUT
  @Path("/{id}")
  public pl.hellopoland.dto.SightEvent update(@PathParam("id") Long id, pl.hellopoland.dto.SightEvent dto) {
    dto.id = id;
    return service.update(dto);
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightEventPagedCollectionConfig config) {
    return service.getList(config);
  }

  @GET
  @Path("/{id}")
  public pl.hellopoland.dto.SightEvent get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public pl.hellopoland.dto.SightEvent uploadIcon(@PathParam("id") Long id, byte[] icon){
    return service.uploadMainImage(id, icon);
  }
}
