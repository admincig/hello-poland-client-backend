package pl.hellopoland.service.api.helpdesk;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import pl.hellopoland.dto.TicketTypeDTO;
import pl.hellopoland.service.HelpdeskAccessService;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketTypeServiceHelpdeskAPI {

  @Inject
  TicketDefinitionService service;
  @Inject
  PartnerService partnerService;
  @Inject
  HelpdeskAccessService accessService;

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public List<TicketTypeDTO> getList(Long partnerId) {
    accessService.requirePartnerAccess(partnerId != null ? partnerService.get(partnerId) : null);
    return service.getTicketTypes(
        partnerId != null ? partnerService.get(partnerId) : service.getLoggedUser());
  }

}
