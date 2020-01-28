package pl.hellopoland.service.api.partner;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketDefinitionDTO;
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

  @RolesAllowed("partner")
  public TicketPoolDefinitionDTO get(Long id) {
    return service.get(id);
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.delete(id);
  }

  @RolesAllowed("partner")
  public List<TicketPoolDefinitionDTO> update(TicketPoolDefinitionDTO dto) {
    for (TicketDefinitionDTO ticketDef : dto.ticketDefinitions) {
      if (ticketDef.discount != null) {
        ticketDef.discount.isCustomCommision = false;
      }
    }
    return service.update(dto);
  }

}
