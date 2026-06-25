package pl.hellopoland.service.api.helpdesk;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import pl.hellopoland.dto.TicketTypeDTO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketTypeServiceHelpdeskAPI {

  @Inject
  TicketDefinitionService service;
  @Inject
  PartnerService partnerService;

  @RolesAllowed({"admin", "salesman"})
  public List<TicketTypeDTO> getList(Long partnerId) {
    return service.getTicketTypes(
        partnerId != null ? partnerService.get(partnerId) : service.getLoggedUser());
  }

}
