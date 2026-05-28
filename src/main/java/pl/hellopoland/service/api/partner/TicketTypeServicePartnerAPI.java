package pl.hellopoland.service.api.partner;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import pl.hellopoland.dto.TicketTypeDTO;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketTypeServicePartnerAPI {

  @Inject
  TicketDefinitionService service;

  @RolesAllowed("partner")
  public List<TicketTypeDTO> getList() {
    return service.getTicketTypes(service.getLoggedPartner());
  }

}
