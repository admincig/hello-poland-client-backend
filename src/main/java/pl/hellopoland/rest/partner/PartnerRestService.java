package pl.hellopoland.rest.partner;

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
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.PartnerServicePartnerAPI;
import pl.hellopoland.service.api.partner.UserServicePartnerAPI;

@Path("/partner")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerRestService {

  @Inject
  private UserServicePartnerAPI userService;
  @Inject
  private PartnerServicePartnerAPI service;

  @POST
  @Path("/ushers")
  public Response createUsher(UserDTO usher) {
    return Response.ok(userService.createUsher(usher)).build();
  }

  @GET
  @Path("/ushers")
  public PagedCollection getUshers() {
    return userService.getUshers();
  }

  @GET
  @Path("/ushers/{id}")
  public Response getUsher(@PathParam("id") long usherId) {
    return Response.ok(userService.getUsher(usherId)).build();
  }

  @PATCH
  @Path("/ushers/{id}")
  public Response updateUsher(@PathParam("id") long usherId, UserDTO usher) {
    usher.id = usherId;
    return Response.ok(userService.updateUsher(usher)).build();
  }

  @PATCH
  @Path("/ushers/{id}/password")
  public Response changePasswordForUsher(@PathParam("id") long usherId, UserAuthDTO usherDTO) {
    userService.changePasswordForUsher(usherId, usherDTO);
    return Response.ok().build();
  }

  @PUT
  @Path("/company/mainImage")
  @Consumes({"image/jpeg", "image/jpg"})
  public MarketPartnerDTO uploadIcon(byte[] icon) {
    return service.uploadMainImage(icon);
  }

  @GET
  @Path("/company/card")
  public MarketPartnerDTO getCard(@HeaderParam("Content-Language") String langString) {
    return service.getCard(RestService.parseLang(langString));
  }

  @POST
  @Path("/company/card")
  public MarketPartnerDTO createLanguageVersion(
      @HeaderParam("Content-Language") String contentLanguage,
      MarketPartnerDTO dto) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.createLanguageVersion(dto, lang);
  }

  @PATCH
  @Path("/company/card/defaultLanguage")
  public MarketPartnerDTO changeDefaultLanguage(
      @HeaderParam("Content-Language") String contentLanguage) {
    LanguageVersion lang = RestService.parseLang(contentLanguage);
    return service.changeDefaultLanguage(lang);
  }

  @PUT
  @Path("/company/card/languageVersion/{language}")
  public MarketPartnerDTO update(@PathParam("language") String language,
      MarketPartnerDTO dto) {
    LanguageVersion lang = RestService.parseLang(language);
    return service.update(dto, lang);
  }

  @DELETE
  @Path("/company/card/languageVersion/{language}")
  public Response deleteLanguageVersion(@PathParam("language") String language) {
    LanguageVersion lang = RestService.parseLang(language);
    service.deleteLanguageVersion(lang);
    return Response.ok().build();
  }

}
