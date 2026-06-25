package pl.hellopoland.rest.helpdesk;

import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.TicketDefinitionServiceHelpdeskAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/helpdesk/ticket-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskTicketDefinitionRestService {

  @Inject
  TicketDefinitionServiceHelpdeskAPI service;

  @POST
  public TicketDefinitionDTO add(TicketDefinitionDTO ticketDefinitionDTO) {
    return service.add(ticketDefinitionDTO);
  }

  @GET
  public PagedCollection<TicketDefinitionDTO> ticketDefinitions(
      @QueryParam("partnerId") Long partnerId) {
    return service.getList(partnerId);
  }

  @GET
  @Path("/{id}")
  public TicketDefinitionDTO get(@PathParam("id") Long id,
      @QueryParam("partnerId") Long partnerId) {
    return service.get(id, partnerId);
  }

  @PUT
  @Path("/{id}")
  public List<TicketDefinitionDTO> update(@PathParam("id") Long id, TicketDefinitionDTO dto) {
    dto.id = id;
    return service.update(dto);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id,
      @QueryParam("partnerId") Long partnerId) {
    service.delete(id, partnerId);
  }

}
