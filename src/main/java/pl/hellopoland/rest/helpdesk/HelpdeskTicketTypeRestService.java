package pl.hellopoland.rest.helpdesk;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import pl.hellopoland.dto.TicketTypeDTO;
import pl.hellopoland.service.api.helpdesk.TicketTypeServiceHelpdeskAPI;

@Path("/helpdesk/ticket-types")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskTicketTypeRestService {

  @Inject
  TicketTypeServiceHelpdeskAPI service;

  @GET
  public List<TicketTypeDTO> getList() {
    return service.getList();
  }

}
