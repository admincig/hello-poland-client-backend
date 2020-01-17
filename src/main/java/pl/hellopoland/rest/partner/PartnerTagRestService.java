package pl.hellopoland.rest.partner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.RestService;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.TagServicePartnerAPI;

@RequestScoped
@Path("/partner/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerTagRestService {

  @Inject
  TagServicePartnerAPI service;

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

}
