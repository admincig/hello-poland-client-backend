package pl.hellopoland.rest.partner;

import java.net.URI;
import java.net.URISyntaxException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.security.CurrentUser;
import pl.hellopoland.service.TicketService;
import pl.hellopoland.util.DtoMapper;

@Path("/partner/ticket-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicketDefinitionRestService {

  @Inject
  private TicketService ticketService;

  @Inject
  private CurrentUser currentUser;

  @POST
  public Response add(pl.hellopoland.dto.TicketDefinition ticketDefinition) throws URISyntaxException {
    return Response.created(new URI("/partner/ticket-definitions/" + ticketDefinition.id))
        .entity(DtoMapper.getDTO(ticketService.create(ticketDefinition, null))).build();
  }

}
