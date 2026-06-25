package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.api.helpdesk.TicketPoolDefinitionServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/helpdesk/ticket-pool-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskTicketPoolDefinitionRestService {

  @Inject
  TicketPoolDefinitionServiceHelpdeskAPI service;

  @POST
  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    return service.add(dto);
  }

  @GET
  public List<TicketPoolDefinitionDTO> list(@QueryParam("partnerId") Long partnerId) {
    return service.list(partnerId);
  }

  @PUT
  @Path("/{id}")
  public TicketPoolDefinitionDTO update(@PathParam("id") Long id,
      TicketPoolDefinitionDTO dto) {
    dto.id = id;
    return service.update(dto);
  }

  @GET
  @Path("/{id}")
  public TicketPoolDefinitionDTO get(@PathParam("id") Long id,
      @QueryParam("partnerId") Long partnerId) {
    return service.get(id, partnerId);
  }

  @DELETE
  @Path("/{id}")
  public void deleteTicketPoolDef(@PathParam("id") Long id,
      @QueryParam("partnerId") Long partnerId) {
    service.delete(id, partnerId);
  }

}
