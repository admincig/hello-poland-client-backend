package pl.hellopoland.rest.partner;

import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.SightServicePartnerAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;

@Path("/partner/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerSightRestService {

  @Inject
  private SightServicePartnerAPI service;

  @POST
  public SightDTO add(SightDTO dto, @HeaderParam("Content-Language") String language) {
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

  @GET
  public PagedCollection<SightDTO> getList(@HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.getList(contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @GET
  @Path("/{id}")
  public SightDTO get(@PathParam("id") Long id,
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.get(id, contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @PUT
  @Path("/{id}/languageVersion/{language}")
  public SightDTO update(@PathParam("id") Long id, @PathParam("language") String language,
      SightDTO dto) {
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
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
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
  public SightDTO changeDefaultLanguage(@PathParam("id") Long id,
      @HeaderParam("Content-Language") String language) {
    LanguageVersion lang =
        Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(language))
            .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    return service.changeDefaultLanguage(id, lang);
  }

}
