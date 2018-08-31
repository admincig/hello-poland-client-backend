package pl.hellopoland.service.api.partner;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketDefinitionServicePartnerAPI {

  @Inject
  TicketDefinitionService service;

  @RolesAllowed("partner")
  public List<TicketDefinitionDTO> getTicketDefinitionsForLoggedUser() {
    return service.getTicketDefinitionsForLoggedUser();
  }

}
