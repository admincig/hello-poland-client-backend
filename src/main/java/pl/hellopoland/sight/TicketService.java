package pl.hellopoland.sight;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.dto.TicketDefinition;
import pl.hellopoland.partner.Partner;
import pl.hellopoland.partner.PartnerService;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.HplMapper;

@Stateless
public class TicketService extends ServiceSuperclass {

  @Inject
  PartnerService partnerService;
  @Inject
  SightEventService sightEventService;


  public Ticket create(TicketDefinition dto, Partner partner){
    if (partner == null) {
      partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    }
    Portal hpt = super.getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    dto = helloTicket.addTicketDefinition(dto, partner.getHptToken());
    Ticket bo = new Ticket();
    HplMapper.copy(dto, bo);
    SightEvent se = sightEventService.get(dto.sightEventId);
    bo.setSightEvent(se);
    em.persist(bo);
    return bo;
  }
}
