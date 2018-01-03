package pl.hellopoland.rest;

import java.io.File;
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
import pl.hellopoland.model.Image;
import pl.hellopoland.service.ImageService;
import pl.hellopoland.service.Service;

@Path("/")
@RequestScoped
public class RestService {

  @Inject
  Service service;
  @Inject
  ImageService iService;

  @Context
  HttpServletRequest req;

  @GET
  public String greetings() {
    return service.greetings() + " and REST Service is running!";
  }

  @POST
  public String login(String email) throws ServletException {
    req.login(email, "");
    service.secured();
    return "security is working";
  }

  @GET
  @Path("/testImage")
  @Produces(MediaType.APPLICATION_JSON)
  public Image testImage() {
    return iService.testImage();
  }

  @GET
  @Path("/images/{name}")
  @Produces({MediaType.APPLICATION_JSON, "image/png", "image/jpg"})
  public Response download(@PathParam("name") String name) {
    File file = iService.getImage(name);
    String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
    return Response.ok().entity(file).type("image/" + extension).build();
  }
}
