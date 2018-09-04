package pl.hellopoland.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.util.HelloTicket;

@Stateless
public class TicketPoolDefinitionService extends ServiceSuperclass {

  @Inject
  SightEventService sightEventService;
  @Inject
  TicketDefinitionService ticketService;

  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    return add(dto, getLoggedPartner());
  }

  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto, Partner partner) {
    final Long sightEventId = dto.sightEventId;
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    SightEvent se = sightEventService.get(dto.sightEventId);
    dto.sightEventId = se.getHptId();
    dto = hpt.addTicketPoolDefinition(dto, partner.getHptToken());
    dto.sightEventId = sightEventId;
    if (dto.ticketDefinitions != null) {
      dto.ticketDefinitions.forEach(td -> {
        TicketDefinition tBo = ticketService.create(td, sightEventId, partner);
        td.id = tBo.getId();
      });
    }
    return dto;
  }

  public TicketPoolDefinitionDTO get(Long id) {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.getTicketPoolDefinition(getLoggedPartner().getHptToken(), id);
  }

  public void delete(Long id) {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    hpt.deleteTicketPoolDefinition(getLoggedPartner().getHptToken(), id);
  }

}
