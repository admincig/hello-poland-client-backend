package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.service.api.helpdesk.PartnerServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/helpdesk/partners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskPartnerRestService {

  @Inject
  private PartnerServiceHelpdeskAPI service;


  @GET
  public Response listPartners(@HeaderParam("Content-Language") String contentLanguage) {
    var config = new PartnerPagedCollectionConfig();
    return Response.ok(service.listPartners(config, RestService.parseLang(contentLanguage)))
        .build();
  }

  @POST
  public PartnerDTO createLanguageVersion(
      @HeaderParam("Content-Language") String contentLanguage,
      PartnerDTO dto) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    if (dto.id != null) {
      return service.createLanguageVersion(dto, lang);
    } else {
      return service.addPartner(dto);
    }
  }

  public record EmailWrapper(String email){}

  @PATCH
  @Path("/{id}/reset")
  public void updateCredentials(@PathParam("id") Long id, EmailWrapper dto) {
    service.resetPartner(id, dto.email());
  }

  @GET
  @Path("/{id}")
  public PartnerDTO get(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    return service.get(id, RestService.parseLang(contentLanguage));
  }

  @PATCH
  @Path("/{id}")
  public PartnerDTO setBlocked(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id,
      PartnerDTO dto) {
    return service.setBlocked(id, dto.blocked, RestService.parseLang(contentLanguage));
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
  public PartnerDTO changeDefaultLanguage(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.changeDefaultLanguage(id, lang);
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg", "image/webp", "image/png"})
  public PartnerDTO uploadIcon(@PathParam("id") Long id, byte[] icon) {
    return service.uploadMainImage(id, icon);
  }

  @PUT
  @Path("/{id}/languageVersion/{language}")
  public PartnerDTO update(@PathParam("id") Long id,
      @PathParam("language") String language,
      PartnerDTO dto) {
    LanguageVersion lang = RestService.parseLang(language);
    dto.id = id;
    return service.update(dto, lang);
  }

}
