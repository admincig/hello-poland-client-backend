package pl.hellopoland.rest.partner;

import java.util.List;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.api.partner.AgreementServicePartnerAPI;

@Path("/partner/agreements")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerAgreementRestService {

  @Inject
  private AgreementServicePartnerAPI service;

  @GET
  public List<AgreementDTO> getForPartner() {
    return service.getForPartner();
  }

  @GET
  @Path("/{id}")
  public AgreementDTO get(@PathParam("id") Long id) {
    return service.getForPartner(id);
  }

  @POST
  public AgreementDTO create(AgreementDTO dto, @HeaderParam("Content-Language") String language) {
    LanguageVersion lang = Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(language))
        .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
    if (dto.id != null) {
      return service.createLanguageVersion(dto, lang);
    }
    return service.create(dto);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

  @PUT
  @Path("/{id}")
  public AgreementDTO update(@PathParam("id") Long id, AgreementDTO dto,
      @QueryParam("language") String language) {
    dto.id = id;
    if (StringUtils.isNotBlank(language)) {
      LanguageVersion lang = Optional.ofNullable(LanguageVersion.getForCreateAndUpdateEntity(language))
          .orElseThrow(() -> new ConflictingException("Unsupported language: " + language));
      return service.updateLanguageVersion(dto, lang);
    }
    return service.update(dto);
  }

}
