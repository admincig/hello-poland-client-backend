package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.CategoryServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/helpdesk/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskCategoryRestService {

  @Inject
  private CategoryServiceHelpdeskAPI service;

  @POST
  public CategoryDTO create(
      @HeaderParam("Content-Language") String contentLanguage,
      CategoryDTO dto) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    if (dto.id == null) {
      dto.language = lang.getLanuage();
      return service.create(dto);
    }
    return service.createLanguageVesrion(dto, lang);
  }

  @GET
  public PagedCollection<CategoryDTO> getCategories(
      @HeaderParam("Content-Language") String contentLanguage) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.pagedList(lang);
  }

  @GET
  @Path("/{id}")
  public CategoryDTO get(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.get(id, lang);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteCategory(
      @PathParam("id") Long id) {
    service.delete(id);
    return Response.ok().build();
  }

  @PUT
  @Path("/{id}/languageVersion/{language}")
  public CategoryDTO update(
      @PathParam("id") Long id,
      @PathParam("language") String language,
      CategoryDTO dto) {
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
  public CategoryDTO changeDefaultLanguage(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.changeDefaultLanguage(id, lang);
  }

  @PUT
  @Path("/{id}/icon")
  @Consumes({"image/jpeg", "image/jpg", "image/png"})
  public CategoryDTO uploadIcon(@PathParam("id") Long id, byte[] bytes,
      @HeaderParam("Content-Type") String contentType) {
    String extension = "jpeg";
    if ("image/png".equals(contentType)) {
      extension = "png";
    }
    return service.uploadIcon(id, bytes, extension);
  }


}
