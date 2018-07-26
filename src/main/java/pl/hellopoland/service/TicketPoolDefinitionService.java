package pl.hellopoland.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.util.HelloTicket;

@Stateless
public class TicketPoolDefinitionService extends ServiceSuperclass {

  @Inject
  SightEventService sightEventService;

  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    Partner partner = getLoggedPartner();
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    SightEvent se = sightEventService.get(dto.sightEventId);
    dto.sightEventId = se.getHptId();
    dto = hpt.addTicketPoolDefinition(dto, partner.getHptToken());
    dto.sightEventId = se.getId();
    return dto;
  }


}
