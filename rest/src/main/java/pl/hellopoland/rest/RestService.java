package pl.hellopoland.rest;

import java.io.File;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.model.Facility;
import pl.hellopoland.service.FacilityService;
import pl.hellopoland.service.ImageService;
import pl.hellopoland.service.Service;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class RestService {

  @Inject
  Service service;
  @Inject
  ImageService iService;
  @Inject
  FacilityService fService;

  @Context
  HttpServletRequest req;

  @POST
  public String login(String email) throws ServletException {
    req.login(email, "");
    service.secured();
    return "security is working";
  }

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
  public PagedCollection<FacilityORO> getList() {
    PagedCollection<FacilityORO> coll = new PagedCollection<>();
    coll.items =
        fService.getList().stream().map(f -> new FacilityORO(f)).collect(Collectors.toList());
    return coll;
  }

  public class FacilityORO {
    public Long id;
    public String name;
    public String mainImage;

    public FacilityORO(Facility f) {
      this.id = f.getId();
      this.name = f.getName();
      this.mainImage = "/images/" + f.getMainImage().getHash() + f.getMainImage().getExtension();
    }
  }
}
