package pl.hellopoland.rest.helpdesk;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.SightServiceHelpdeskAPI;

@RequestScoped
@Path("/helpdesk/sights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskSightRestService {

  @Inject
  SightServiceHelpdeskAPI service;


  @POST
  public SightDTO createLanguageVersion(
      @HeaderParam("Content-Language") String contentLanguage,
      SightDTO dto) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.createLanguageVesrion(dto, lang);
  }

  @GET
  public PagedCollection list(
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.list(new SightPagedCollectionConfig(),
        RestService.parseLang(contentLanguage));
  }

  @DELETE
  @Path("/{id}")
  public Response delete(@PathParam("id") Long id) {
    service.delete(id);
    return Response.ok().build();
  }

  @GET
  @Path("/{id}")
  public SightDTO get(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    return service.get(id, RestService.parseLang(contentLanguage));
  }

  @DELETE
  @Path("/{id}/languageVersion/{language}")
  public Response deleteLanguageVersion(
      @PathParam("id") Long id,
      @PathParam("language") String language) {
    LanguageVersion lang = RestService.parseLang(language);
    service.deleteLanguageVersion(id, lang);
    return Response.ok().build();
  }

  @PATCH
  @Path("/{id}/defaultLanguage")
  public SightDTO changeDefaultLanguage(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.changeDefaultLanguage(id, lang);
  }

  @PUT
  @Path("/{id}/languageVersion/{language}")
  public SightDTO update(@PathParam("id") Long id,
      @PathParam("language") String language,
      SightDTO dto) {
    LanguageVersion lang = RestService.parseLang(language);
    dto.id = id;
    return service.update(dto, lang);
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public SightDTO uploadIcon(@PathParam("id") Long id, byte[] icon) {
    return service.uploadMainImage(id, icon);
  }

  @POST
  @Path("/{id}/images")
  @Consumes({"image/jpeg", "image/jpg"})
  public SightEventDTO uploadImage(@PathParam("id") Long id, byte[] img) {
    return service.uploadImage(id, img);
  }

  @DELETE
  @Path("/{id}/images/{imgId}")
  public SightDTO deleteImage(@PathParam("id") Long id, @PathParam("imgId") Long imgId) {
    return service.deleteImage(id, imgId);
  }
}
