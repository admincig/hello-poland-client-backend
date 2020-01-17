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
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.TagServiceHelpdeskAPI;

@RequestScoped
@Path("/helpdesk/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskTagRestService {

  @Inject
  private TagServiceHelpdeskAPI service;

  @POST
  public TagDTO create(
      @HeaderParam("Content-Language") String contentLanguage,
      TagDTO dto) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    if (dto.id == null) {
      dto.language = lang.getLanuage();
      return service.create(dto);
    }
    return service.createLanguageVesrion(dto, lang);
  }

  @GET
  public PagedCollection<TagDTO> getCategories(
      @HeaderParam("Content-Language") String contentLanguage) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.pagedList(lang);
  }

  @GET
  @Path("/{id}")
  public TagDTO get(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.get(id, lang);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteTag(
      @PathParam("id") Long id) {
    service.delete(id);
    return Response.ok().build();
  }

  @PUT
  @Path("/{id}/languageVersion/{language}")
  public TagDTO update(
      @PathParam("id") Long id,
      @PathParam("language") String language,
      TagDTO dto) {
    LanguageVersion lang = RestService.parseLang(language);
    dto.id = id;
    return service.update(dto, lang);
  }

  @DELETE
  @Path("/{id}/languageVersion/{language}")
  public Response delete(
      @PathParam("id") Long id,
      @PathParam("language") String language) {
    LanguageVersion lang = RestService.parseLang(language);
    service.deleteLanguageVersion(id, lang);
    return Response.ok().build();
  }

  @PATCH
  @Path("/{id}/defaultLanguage")
  public TagDTO changeDefaultLanguage(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.changeDefaultLanguage(id, lang);
  }

}
