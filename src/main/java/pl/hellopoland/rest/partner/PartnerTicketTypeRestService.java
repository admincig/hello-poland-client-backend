package pl.hellopoland.rest.partner;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import pl.hellopoland.dto.TicketTypeDTO;
import pl.hellopoland.service.api.partner.TicketTypeServicePartnerAPI;

@Path("/partner/ticket-types")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerTicketTypeRestService {

  @Inject
  TicketTypeServicePartnerAPI service;

  @GET
  public List<TicketTypeDTO> getList() {
    return service.getList();
  }

}
