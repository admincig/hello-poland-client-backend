package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.TagServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    @PUT
    @Path("/{id}/icon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public TagDTO uploadIcon(@PathParam("id") Long id, MultipartFormDataInput input) throws Exception {

        Map<String, List<InputPart>> form = input.getFormDataMap();

        // FE zwykle wysyła pole "file" (czasem "icon")
        InputPart part = null;
        if (form.containsKey("file") && !form.get("file").isEmpty()) part = form.get("file").get(0);
        else if (form.containsKey("icon") && !form.get("icon").isEmpty()) part = form.get("icon").get(0);
        else throw new BadRequestException("Missing multipart field: file/icon");

        String contentType =
                part.getMediaType() != null ? part.getMediaType().toString().toLowerCase() : "image/jpeg";

        try (InputStream is = part.getBody(InputStream.class, null)) {
            byte[] bytes = is.readAllBytes();

            String extension = "jpeg";
            if (contentType.startsWith("image/png")) extension = "png";
            else if (contentType.startsWith("image/svg+xml")) extension = "svg";
            else if (contentType.startsWith("image/jpg") || contentType.startsWith("image/jpeg")) extension = "jpeg";
            else throw new NotSupportedException("Unsupported content type: " + contentType);

            return service.uploadIcon(id, bytes, extension);
        }
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
