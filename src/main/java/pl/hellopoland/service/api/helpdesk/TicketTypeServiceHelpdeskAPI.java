package pl.hellopoland.service.api.helpdesk;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import pl.hellopoland.dto.TicketTypeDTO;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketTypeServiceHelpdeskAPI {

  @Inject
  TicketDefinitionService service;

  @RolesAllowed("admin")
  public List<TicketTypeDTO> getList() {
    return service.getTicketTypes(service.getLoggedUser());
  }

}
