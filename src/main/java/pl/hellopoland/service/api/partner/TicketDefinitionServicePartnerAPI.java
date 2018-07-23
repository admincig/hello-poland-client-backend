package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Ticket;
import pl.hellopoland.service.TicketService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class TicketDefinitionServicePartnerAPI {

  @Inject
  TicketService service;

  @RolesAllowed("partner")
  public pl.hellopoland.dto.TicketDefinition add(pl.hellopoland.dto.TicketDefinition dto){
    Ticket bo = service.create(dto, null);
    dto = DtoMapper.getDTO(bo);
    return dto;
  }
}
