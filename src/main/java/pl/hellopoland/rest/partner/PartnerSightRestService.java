package pl.hellopoland.rest.partner;

import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.SightServicePartnerAPI;

@Path("/partner/sights")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerSightRestService {

  @Inject
  private SightServicePartnerAPI service;

  @POST
  public SightDTO add(SightDTO dto, @HeaderParam("Content-Language") String language) {
    LanguageVersion lang = Optional.ofNullable(LanguageVersion.getForCreateEntity(language))
        .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    if (dto.id == null) {
      dto.defaultLanguage = lang.getLanuage();
      return service.create(dto);
    }
    return service.createLanguageVesrion(dto, lang);
  }

  @GET
  public PagedCollection getList() {
    return service.getList();
  }

  @GET
  @Path("/{id}")
  public SightDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @PUT
  @Path("/{id}")
  public SightDTO update(@PathParam("id") Long id, SightDTO dto,
      @HeaderParam("Content-Language") String language) {
    dto.id = id;
    if (StringUtils.isNotBlank(language)) {
      return service.updateLanguageVersion(dto, language);
    }
    return service.update(dto);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public SightDTO uploadMainImage(@PathParam("id") Long id, byte[] icon) {
    return service.uploadMainImage(id, icon);
  }

  @POST
  @Path("/{id}/image")
  @Consumes({"image/jpeg", "image/jpg"})
  public SightDTO uploadImage(@PathParam("id") Long id, byte[] img) {
    return service.uploadImage(id, img);
  }

  @DELETE
  @Path("/{id}/images/{imgId}")
  public SightDTO uploadImage(@PathParam("id") Long id, @PathParam("imgId") Long imgId) {
    return service.removeImageFromGallery(id, imgId);
  }
}
