package pl.hellopoland.rest;

import java.io.File;
import java.lang.System.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.service.ImageService;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestService {

  Logger logger = System.getLogger(RestService.class.getName());

  @Inject
  ImageService imageService;

  @POST
  @Path("/anything")
  @Consumes("*/*")
  public void anything(@Context HttpHeaders headers, String anything) {
    logger.log(Logger.Level.INFO, "Anything - headers: " + headers.getRequestHeaders());
    logger.log(Logger.Level.INFO, "Anything - body: " + anything);
  }

  @GET
  @Path("/images/{name}")
  @Produces({MediaType.APPLICATION_JSON, "image/png", "image/jpg"})
  public Response download(@PathParam("name") String name) {
    File file = imageService.getImage(name);
    String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
    return Response.ok().entity(file).type("image/" + extension).build();
  }

}
