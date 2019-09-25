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
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.service.api.helpdesk.PartnerServiceHelpdeskAPI;

@RequestScoped
@Path("/helpdesk/partners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskPartnerRestService {

  @Inject
  private PartnerServiceHelpdeskAPI service;

  @POST
  public PartnerDTO add(PartnerDTO partner) {
    return service.addPartner(partner);
  }

  @GET
  public Response listPartners() {
    var config = new PartnerPagedCollectionConfig();
    return Response.ok(service.listPartners(config)).build();
  }

  @POST
  public PartnerDTO createLanguageVersion(
      @HeaderParam("Content-Language") String contentLanguage,
      PartnerDTO dto) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.createLanguageVersion(dto, lang);
  }

  @GET
  @Path("/{id}")
  public PartnerDTO get(
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
  public PartnerDTO changeDefaultLanguage(
      @HeaderParam("Content-Language") String contentLanguage,
      @PathParam("id") Long id) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.changeDefaultLanguage(id, lang);
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
