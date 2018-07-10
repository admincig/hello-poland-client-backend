package pl.hellopoland.rest.partner;

import static javax.ws.rs.core.Response.noContent;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;
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
import javax.ws.rs.core.Response;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.sight.SightService;
import pl.hellopoland.sight.TicketService;
import pl.hellopoland.util.HplMapper;

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
        .entity(HplMapper.getDTO(ticketService.create(ticketDefinition, null))).build();
  }

}
