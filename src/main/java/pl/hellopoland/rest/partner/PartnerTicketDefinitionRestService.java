package pl.hellopoland.rest.partner;

import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.api.partner.TicketDefinitionServicePartnerAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/partner/ticket-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerTicketDefinitionRestService {

  @Inject
  TicketDefinitionServicePartnerAPI service;

  @POST
  public TicketDefinitionDTO add(TicketDefinitionDTO ticketDefinitionDTO) {
    return service.add(ticketDefinitionDTO, null);
  }

  @GET
  public PagedCollection<TicketDefinitionDTO> ticketDefinitions() {
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
