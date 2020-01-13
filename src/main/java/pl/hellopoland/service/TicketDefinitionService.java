package pl.hellopoland.service;

import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.HptSubject;
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

  public TicketDefinition create(TicketDefinitionDTO dto, Long sightEventId) {
    if (dto.price < 0) {
      throw new BadRequestException("The ticket price must be greater than 0");
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

  public TicketDefinition findByExternalIdAndPartner(Long externalId, Partner partner) {
    return em
        .createQuery(
            "from TicketDefinition where externalId = :externalId and sightEvent.partner=:partner",
            TicketDefinition.class)
        .setParameter("externalId", externalId)
        .setParameter("partner", partner)
        .getSingleResult();
  }

  public TicketDefinition findByExternalId(Long externalId) {
    return em
        .createQuery(
            "from TicketDefinition where externalId = :externalId",
            TicketDefinition.class)
        .setParameter("externalId", externalId)
        .getSingleResult();
  }

  public List<TicketDefinitionDTO> getTicketDefinitions(HptSubject hptSubject) {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.getTicketDefinitions(hptSubject.getHptToken());
  }

  public TicketDefinitionDTO add(TicketDefinitionDTO dto, HptSubject hptSubject) {
    if (dto.price < 0) {
      throw new BadRequestException("The ticket price must be greater than 0");
    }
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.addTicketDefinition(dto, hptSubject.getHptToken());
  }

  public List<TicketDefinitionDTO> update(TicketDefinition td, TicketDefinitionDTO dto,
      HptSubject hptSubject) {
    if (dto.price < 0) {
      throw new BadRequestException("The ticket price must be greater than 0");
    }
    td.setName(dto.name);
    td.setPrice(dto.price);
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.updateTicketDefinition(dto, hptSubject.getHptToken());
  }

  public void delete(Long id, HptSubject hptSubject) {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    hpt.deleteTicketDefinition(hptSubject.getHptToken(), id);
  }

  public TicketDefinitionDTO getTicketDefinition(Long id, HptSubject hptSubject) {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    return hpt.getTicketDefinition(id, hptSubject.getHptToken());
  }

}
