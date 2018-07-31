package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Ticket;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.service.TicketService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class TicketDefinitionServicePartnerAPI {

  @Inject
  TicketService service;

  @RolesAllowed("partner")
  public TicketDefinitionDTO add(TicketDefinitionDTO dto) {
    Ticket bo = service.create(dto, null);
    dto = DtoMapper.getDTO(bo);
    return dto;
  }
}
