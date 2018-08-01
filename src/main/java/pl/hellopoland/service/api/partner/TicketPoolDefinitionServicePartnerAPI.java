package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.TicketPoolDefinitionService;

@Stateless
public class TicketPoolDefinitionServicePartnerAPI {

  @Inject
  TicketPoolDefinitionService service;

  @RolesAllowed("partner")
  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    return service.add(dto);
  }

}
