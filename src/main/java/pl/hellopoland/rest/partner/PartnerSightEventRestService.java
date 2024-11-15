package pl.hellopoland.rest.partner;

import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.SightEventServicePartnerAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Path("/partner/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerSightEventRestService {

  @Inject
  SightEventServicePartnerAPI service;

  @GET
  public PagedCollection<SightEventDTO> getList(
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.getList(new SightEventPagedCollectionConfig(),
        contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @POST
  public SightEventDTO create(SightEventDTO dto, @HeaderParam("Content-Language") String language) {
    LanguageVersion lang =
        Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(language))
            .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    if (dto.id == null) {
      dto.defaultLanguage = lang.getLanuage();
      dto.availableLanguageVersions = Set.of(lang.getLanuage());
      return service.create(dto);
    }
    return service.createLanguageVesrion(dto, lang);
  }

  @PUT
  @Path("/{id}/languageVersion/{language}")
  public SightEventDTO update(@PathParam("id") Long id, @PathParam("language") String language,
      SightEventDTO dto) {
    if (StringUtils.isBlank(language)) {
      throw new ConflictingException("Language is required");
    }
    LanguageVersion lang =
        Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(language))
            .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    dto.id = id;
    return service.update(dto, lang);
  }

  @DELETE
  @Path("/{id}/languageVersion/{language}")
  public Response delete(@PathParam("id") Long id, @PathParam("language") String language) {
    LanguageVersion lang =
        Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(language))
            .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    service.delete(id, lang);
    return Response.ok().build();
  }

  @PATCH
  @Path("/{id}/defaultLanguage")
  public SightEventDTO changeDefaultLanguage(@PathParam("id") Long id,
      @HeaderParam("Content-Language") String language) {
    LanguageVersion lang =
        Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(language))
            .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    return service.changeDefaultLanguage(id, lang);
  }

  @GET
  @Path("/{id}")
  public SightEventDTO get(@PathParam("id") Long id,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.get(id, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

  @DELETE
  @Path("/{id}/sale")
  public void stopSale(@PathParam("id") Long id, @QueryParam("tpdId") Long tpdId,
      @QueryParam("date") @DateFormat Date date) {
    service.stopSale(id, tpdId, date);
  }

  @PATCH
  @Path("/{id}/categories/{cId}")
  public SightEventDTO addCategory(
      @PathParam("id") Long id,
      @PathParam("cId") Long categoryId) {
    return service.addCategory(id, categoryId);
  }

  @DELETE
  @Path("/{id}/categories/{cId}")
  public SightEventDTO removeCategory(
      @PathParam("id") Long id,
      @PathParam("cId") Long categoryId) {
    return service.removeCategory(id, categoryId);
  }

  @PATCH
  @Path("/{id}/tags/{tId}")
  public SightEventDTO addTag(
      @PathParam("id") Long id,
      @PathParam("tId") Long tagId) {
    return service.addTag(id, tagId);
  }

  @DELETE
  @Path("/{id}/tags/{tId}")
  public SightEventDTO removeTag(
      @PathParam("id") Long id,
      @PathParam("tId") Long tagId) {
    return service.removeTag(id, tagId);
  }

}
