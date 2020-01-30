package pl.hellopoland.rest.partner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.api.partner.TicketPoolDefinitionServicePartnerAPI;

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
