package pl.hellopoland.rest.partner;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.service.api.partner.TicketDefinitionServicePartnerAPI;

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
  public List<TicketDefinitionDTO> ticketDefinitions() {
    return service.getTicketDefinitionsForLoggedUser();
  }

}
