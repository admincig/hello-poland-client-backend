package pl.hellopoland.rest.partner;

import java.util.Date;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.annotation.DateTimeFormat;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.SightEventServicePartnerAPI;

@Path("/partner/sight-events")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerSightEventRestService {

  @Inject
  SightEventServicePartnerAPI service;

  @GET
  public PagedCollection getList() {
    return search(new SightEventPagedCollectionConfig());
  }

  @POST
  public SightEventDTO create(SightEventDTO dto, @QueryParam("language") String language) {
    LanguageVersion defLang = Optional.ofNullable(LanguageVersion.getLanuageVersion(language))
        .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    if (dto.id == null) {
      dto.defaultLanguage = defLang.getLanuage();
      return service.create(dto);
    }
    return service.createLanguageVesrion(dto, language);
  }

  @PUT
  @Path("/{id}")
  public SightEventDTO update(@PathParam("id") Long id, SightEventDTO dto,
      @QueryParam("language") String language) {
    dto.id = id;
    if (StringUtils.isNotBlank(language)) {
      return service.updateLanguageVersion(dto, language);
    }
    return service.update(dto);
  }

  @POST
  @Path("/search")
  public PagedCollection search(SightEventPagedCollectionConfig config) {
    return service.getList(config);
  }

  @GET
  @Path("/{id}")
  public SightEventDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

  @PUT
  @Path("/{id}/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public SightEventDTO uploadIcon(@PathParam("id") Long id, byte[] icon) {
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
  public SightEventDTO deleteImage(@PathParam("id") Long id, @PathParam("imgId") Long imgId) {
    return service.removeImageFromGallery(id, imgId);
  }

  @POST
  @Path("/{id}/pdf")
  @Consumes("application/pdf")
  public SightEventDTO uploadPdf(@PathParam("id") Long id, byte[] pdf) {
    return service.uploadPdf(id, pdf);
  }

  @DELETE
  @Path("/{id}/pdf")
  @Consumes("application/pdf")
  public void deletePdf(@PathParam("id") Long id) {
    service.deletePdf(id);
  }

  @DELETE
  @Path("/{id}/sale")
  public void stopSale(@PathParam("id") Long id, @QueryParam("tpdId") Long tpdId,
      @QueryParam("date") @DateTimeFormat Date date) {
    service.stopSale(id, tpdId, date);
  }

}
