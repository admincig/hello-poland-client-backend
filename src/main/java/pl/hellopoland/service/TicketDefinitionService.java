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
import pl.hellopoland.exception.badrequest.BadRequestException;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;

@Stateless
public class TicketDefinitionService extends ServiceSuperclass {

  @Inject
  PartnerService partnerService;
  @Inject
  SightEventService sightEventService;

  public TicketDefinition create(TicketDefinitionDTO dto, Long sightEventId, Partner partner) {
    if (dto.price < 0) {
      throw new BadRequestException("The ticket price must be greater than 0");
    }
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
    return em
        .createQuery("from TicketDefinition t where t.externalId in (:ids)", TicketDefinition.class)
        .setParameter("ids", externalIds).getResultList();
  }

  private TicketDefinition findByExternalIdAndPartner(Long externalId, Partner partner) {
    return em
        .createQuery(
            "from TicketDefinition where externalId = :externalId and sightEvent.partner=:partner",
            TicketDefinition.class)
        .setParameter("externalId", externalId)
        .setParameter("partner", partner)
        .getSingleResult();
  }

  public List<TicketDefinitionDTO> getTicketDefinitions(Partner partner) {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.getTicketDefinitions(partner.getHptToken());
  }

  public TicketDefinitionDTO add(TicketDefinitionDTO dto, Partner partner) {
    if (dto.price < 0) {
      throw new BadRequestException("The ticket price must be greater than 0");
    }
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.addTicketDefinition(dto, partner.getHptToken());
  }

  public List<TicketDefinitionDTO> update(TicketDefinitionDTO dto, Partner partner) {
    if (dto.price < 0) {
      throw new BadRequestException("The ticket price must be greater than 0");
    }
    TicketDefinition td = findByExternalIdAndPartner(dto.id, partner);
    td.setName(dto.name);
    td.setPrice(dto.price);
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.updateTicketDefinition(dto, partner.getHptToken());
  }

  public void delete(Long id, Partner partner) {
    TicketDefinition td = findByExternalIdAndPartner(id, partner);
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    hpt.deleteTicketDefinition(partner.getHptToken(), td.getExternalId());
  }

  public TicketDefinitionDTO getTicketDefinition(Long id, Partner partner) {
    TicketDefinition td = findByExternalIdAndPartner(id, partner);
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.getTicketDefinition(td.getExternalId(), partner.getHptToken());
  }

}
