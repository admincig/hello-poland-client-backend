package pl.hellopoland.rest.helpdesk;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.dto.CategoryDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.CategoryServiceHelpdeskAPI;

@RequestScoped
@Path("/helpdesk/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskCategoryRestService {

  @Inject
  private CategoryServiceHelpdeskAPI service;

  @POST
  public CategoryDTO create(CategoryDTO dto) {
    return service.create(dto);
  }

  @GET
  public PagedCollection getCategories(
      @HeaderParam("Accept-Language") String acceptLanguage,
      @HeaderParam("Content-Language") String contentLanguage) {
    return service.pagedList(contentLanguage != null ? contentLanguage : acceptLanguage);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteCategory(
      @PathParam("id") Long id) {
    service.delete(id);
    return Response.ok().build();
  }

}
