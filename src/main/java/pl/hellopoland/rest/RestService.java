package pl.hellopoland.rest;

import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.FileDescriptorService;
import pl.hellopoland.service.ImageService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.lang.System.Logger;
import java.util.Optional;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestService {

  Logger logger = System.getLogger(RestService.class.getName());

  @Inject
  ImageService imageService;
  @Inject
  FileDescriptorService fileService;

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
    CacheControl cc = new CacheControl();
    cc.setMaxAge(31536000);
    cc.setPrivate(true);
    String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
    return Response.ok().entity(file).cacheControl(cc).type("image/" + extension).build();
  }

  @GET
  @Path("/files/{path}")
  @Produces({MediaType.APPLICATION_JSON, "application/pdf"})
  public Response downloadFile(@PathParam("path") String path) {
    File file = fileService.getFile(path);
    String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
    return Response.ok().entity(file).type("application/" + extension).build();
  }

  @GET
  @Path("/ping")
  public Response ping() {
    return Response.ok().build();
  }

  public static LanguageVersion parseLang(String contentLanguage) {
    if (StringUtils.isBlank(contentLanguage)) {
      return LanguageVersion.PL_PL;
    }
    return Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(contentLanguage))
        .orElseThrow(() -> new ConflictingException("Unsupported language: " + contentLanguage));
  }

}
