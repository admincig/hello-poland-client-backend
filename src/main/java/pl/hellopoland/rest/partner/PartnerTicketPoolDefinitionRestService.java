package pl.hellopoland.rest.partner;

import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.api.partner.TicketPoolDefinitionServicePartnerAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/partner/ticket-pool-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerTicketPoolDefinitionRestService {

  @Inject
  TicketPoolDefinitionServicePartnerAPI service;

  @POST
  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    return service.add(dto);
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
  public TicketPoolDefinitionDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @DELETE
  @Path("/{id}")
  public void deleteTicketPoolDef(@PathParam("id") Long id) {
    service.delete(id);
  }

}
