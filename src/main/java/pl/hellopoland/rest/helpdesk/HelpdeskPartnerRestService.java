package pl.hellopoland.rest.helpdesk;

import jakarta.validation.Valid;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.service.api.helpdesk.PartnerServiceHelpdeskAPI;
import pl.hellopoland.service.api.helpdesk.UserServiceHelpdeskAPI;

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

  @Inject
  private UserServiceHelpdeskAPI userService;


  @GET
  public Response listPartners(@HeaderParam("Content-Language") String contentLanguage) {
    var config = new PartnerPagedCollectionConfig();
    return Response.ok(service.listPartners(config, RestService.parseLang(contentLanguage)))
        .build();
  }

  @POST
  public PartnerDTO createLanguageVersion(
      @HeaderParam("Content-Language") String contentLanguage,
      @Valid PartnerDTO dto) {
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
  @Path("/{id}/users")
  public Response listUsers(@PathParam("id") Long id) {
    return Response.ok(userService.getPartnerUsers(id)).build();
  }

  @POST
  @Path("/{id}/users")
  public Response createUser(@PathParam("id") Long id, @Valid UserDTO dto) {
    return Response.ok(userService.createPartnerUser(id, dto)).build();
  }

  @PUT
  @Path("/{id}/users/{userId}")
  public Response updateUser(@PathParam("id") Long id, @PathParam("userId") Long userId,
      @Valid UserDTO dto) {
    return Response.ok(userService.updatePartnerUser(id, userId, dto)).build();
  }

  @PATCH
  @Path("/{id}/users/{userId}/password")
  public Response changeUserPassword(@PathParam("id") Long id, @PathParam("userId") Long userId,
      UserDTO dto) {
    userService.changePartnerUserPassword(id, userId, dto.password);
    return Response.ok().build();
  }

  @PATCH
  @Path("/{id}/users/{userId}/blocked")
  public Response setUserBlocked(@PathParam("id") Long id, @PathParam("userId") Long userId,
      UserDTO dto) {
    boolean blocked = Boolean.TRUE.equals(dto.blocked);
    return Response.ok(userService.setPartnerUserBlocked(id, userId, blocked)).build();
  }

  @DELETE
  @Path("/{id}/users/{userId}")
  public Response deleteUser(@PathParam("id") Long id, @PathParam("userId") Long userId) {
    userService.deletePartnerUser(id, userId);
    return Response.noContent().build();
  }

  @GET
  @Path("/{id}/ushers")
  public Response listUshers(@PathParam("id") Long id) {
    return Response.ok(userService.getPartnerUshers(id)).build();
  }

  @POST
  @Path("/{id}/ushers")
  public Response createUsher(@PathParam("id") Long id, @Valid UserDTO dto) {
    return Response.ok(userService.createPartnerUsher(id, dto)).build();
  }

  @PATCH
  @Path("/{id}/ushers/{usherId}")
  public Response updateUsher(@PathParam("id") Long id, @PathParam("usherId") Long usherId,
      @Valid UserDTO dto) {
    return Response.ok(userService.updatePartnerUsher(id, usherId, dto)).build();
  }

  @PATCH
  @Path("/{id}/ushers/{usherId}/password")
  public Response changeUsherPassword(@PathParam("id") Long id, @PathParam("usherId") Long usherId,
      UserDTO dto) {
    userService.changePartnerUsherPassword(id, usherId, dto.password);
    return Response.ok().build();
  }

  @PATCH
  @Path("/{id}/ushers/{usherId}/blocked")
  public Response setUsherBlocked(@PathParam("id") Long id, @PathParam("usherId") Long usherId,
      UserDTO dto) {
    boolean blocked = Boolean.TRUE.equals(dto.blocked);
    return Response.ok(userService.setPartnerUsherBlocked(id, usherId, blocked)).build();
  }

  @DELETE
  @Path("/{id}/ushers/{usherId}")
  public Response deleteUsher(@PathParam("id") Long id, @PathParam("usherId") Long usherId) {
    userService.deletePartnerUsher(id, usherId);
    return Response.noContent().build();
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
      @Valid PartnerDTO dto) {
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
  public PartnerDTO uploadIcon(@PathParam("id") Long id, byte[] icon, @HeaderParam("Content-Type") String contentType) {
    String extension = "jpeg";
    if ("image/png".equals(contentType)) {
      extension = "png";
    } else if ("image/webp".equals(contentType)) {
      extension = "webp";
    }
    return service.uploadMainImage(id, icon, extension);
  }

  @PUT
  @Path("/{id}/languageVersion/{language}")
  public PartnerDTO update(@PathParam("id") Long id,
      @PathParam("language") String language,@Valid PartnerDTO dto) {
    LanguageVersion lang = RestService.parseLang(language);
    dto.id = id;
    return service.update(dto, lang);
  }

}
