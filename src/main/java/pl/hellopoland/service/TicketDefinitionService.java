package pl.hellopoland.service;

import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;

@Stateless
public class TicketDefinitionService extends ServiceSuperclass {

  @Inject
  PartnerService partnerService;
  @Inject
  SightEventService sightEventService;


  public TicketDefinition create(TicketDefinitionDTO dto, Long sightEventId, Partner partner) {
    if (partner == null) {
      partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    }
    TicketDefinition bo = new TicketDefinition();
    DtoMapper.copy(dto, bo);
    SightEvent se = sightEventService.get(sightEventId);
    bo.setSightEvent(se);
    em.persist(bo);
    return bo;
  }


  public List<TicketDefinition> getTicketsByExternalIds(List<Long> externalIds) {
    if (externalIds.isEmpty()) {
      return Collections.emptyList();
    }
    return em.createQuery("from Ticket t where t.externalId in (:ids)", TicketDefinition.class)
        .setParameter("ids", externalIds).getResultList();
  }

  public List<TicketDefinitionDTO> getTicketDefinitionsForLoggedUser() {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.getTicketDefinitions(getLoggedPartner().getHptToken());
  }

}
