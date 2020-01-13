package pl.hellopoland.rest.helpdesk;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.helpdesk.TicketDefinitionServiceHelpdeskAPI;

@Path("/helpdesk/ticket-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskTicketDefinitionRestService {

  @Inject
  TicketDefinitionServiceHelpdeskAPI service;

  @GET
  public PagedCollection ticketDefinitions() {
    return service.getList();
  }

  @GET
  @Path("/{id}")
  public TicketDefinitionDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @PUT
  @Path("/{id}")
  public List<TicketDefinitionDTO> update(@PathParam("id") Long id, TicketDefinitionDTO dto) {
    dto.id = id;
    return service.update(dto);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

}
