package pl.hellopoland.rest.partner;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.exceptionhandler.ConflictingExceptionMapper;
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

  @GET
  @Path("/{id}")
  public TicketPoolDefinitionDTO get(@PathParam("id") Long id) {
    return service.get(id);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteTicketPoolDef(@PathParam("id") Long id) {
    return service.delete(id) ? Response.noContent().build()
        : new ConflictingExceptionMapper().toResponse(new ConflictingException(""));
  }

}
