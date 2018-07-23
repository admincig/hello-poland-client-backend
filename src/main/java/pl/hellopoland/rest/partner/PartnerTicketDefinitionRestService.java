package pl.hellopoland.rest.partner;

import java.net.URI;
import java.net.URISyntaxException;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.bo.Ticket;
import pl.hellopoland.dto.TicketDefinition;
import pl.hellopoland.security.CurrentUser;
import pl.hellopoland.service.TicketService;
import pl.hellopoland.service.api.partner.TicketDefinitionServicePartnerAPI;
import pl.hellopoland.util.DtoMapper;

@Path("/partner/ticket-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerTicketDefinitionRestService {

  @Inject
  TicketDefinitionServicePartnerAPI service;

  @POST
  public pl.hellopoland.dto.TicketDefinition add(pl.hellopoland.dto.TicketDefinition dto) {
    return service.add(dto);
  }

}
