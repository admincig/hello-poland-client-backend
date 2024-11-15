package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.SightServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
  public PagedCollection<SightDTO> list(
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

}
