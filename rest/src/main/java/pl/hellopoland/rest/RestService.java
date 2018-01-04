package pl.hellopoland.rest;

import java.io.File;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.config.FacilityPagedCollectionConfig;
import pl.hellopoland.model.Facility;
import pl.hellopoland.rest.dto.FacilityORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.FacilityService;
import pl.hellopoland.service.ImageService;
import pl.hellopoland.util.PagedEntityCollection;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class RestService {

  @Inject
  ImageService iService;
  @Inject
  FacilityService fService;

  @GET
  @Path("/images/{name}")
  @Produces({MediaType.APPLICATION_JSON, "image/png", "image/jpg"})
  public Response download(@PathParam("name") String name) {
    File file = iService.getImage(name);
    String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
    return Response.ok().entity(file).type("image/" + extension).build();
  }

  @GET
  @Path("/facilities")
  public PagedCollection getList() {
    return search(new FacilityPagedCollectionConfig());
  }

  @POST
  @Path("/facilities/search")
  public PagedCollection search(FacilityPagedCollectionConfig config) {
    PagedEntityCollection<Facility> plist = fService.getList(config);
    return new PagedCollection(
        plist.items.stream().map(f -> new FacilityORO(f)).collect(Collectors.toList()),
        plist.config);
  }

}
