package pl.hellopoland.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.Ticket;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;

@Stateless
public class TicketService extends ServiceSuperclass {

  @Inject
  PartnerService partnerService;
  @Inject
  SightEventService sightEventService;


  public Ticket create(TicketDefinitionDTO dto, Partner partner) {
    if (partner == null) {
      partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    }
    Portal hpt = super.getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    dto = helloTicket.addTicketDefinition(dto, partner.getHptToken());
    Ticket bo = new Ticket();
    DtoMapper.copy(dto, bo);
    // SightEvent se = sightEventService.get(dto. .sightEventId);
    // bo.setSightEvent(se);
    em.persist(bo);
    return bo;
  }
}
