package pl.hellopoland.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.Ticket;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.util.HelloTicket;

@Stateless
public class TicketPoolDefinitionService extends ServiceSuperclass {

  @Inject
  SightEventService sightEventService;
  @Inject
  TicketService ticketService;

  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    return add(dto, null);
  }

  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto, Partner partnerArg) {
    final Long sightEventId = dto.sightEventId;
    Partner partner = partnerArg == null ? getLoggedPartner() : partnerArg;
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    SightEvent se = sightEventService.get(dto.sightEventId);
    dto.sightEventId = se.getHptId();
    dto = hpt.addTicketPoolDefinition(dto, partner.getHptToken());
    dto.sightEventId = sightEventId;
    dto.ticketDefinitions.forEach(td -> {
      Ticket tBo = ticketService.create(td, sightEventId, partner);
      td.id = tBo.getId();
    });
    return dto;
  }

}
