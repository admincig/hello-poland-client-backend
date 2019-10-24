package pl.hellopoland.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.AccessDeniedException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
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
    SightEvent se = sightEventService.get(dto.sightEventId);
    if (se == null) {
      throw new ResourceNotFoundException();
    }
    if (!se.getPartner().getId().equals(partner.getId())) {
      throw new AccessDeniedException();
    }
    validateDates(dto);
    dto.sightEventId = se.getHptId();
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
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

  private void validateDates(TicketPoolDefinitionDTO tpdDTO) {
    if (tpdDTO.endDate != null && tpdDTO.startDate.after(tpdDTO.endDate)) {
      throw new ConflictingException("Ticket pool definition's startDate after endDate.");
    }
    if (tpdDTO.entryEndDate != null && tpdDTO.entryStartDate != null
        && tpdDTO.entryStartDate.after(tpdDTO.entryEndDate)) {
      throw new ConflictingException("Ticket pool definition's entryStartDate after entryEndDate.");
    }
    if (tpdDTO.entryStartDate != null && tpdDTO.startDate != null
        && tpdDTO.entryStartDate.after(tpdDTO.startDate)) {
      throw new ConflictingException("Ticket pool definition's entryStartDate after startDate.");
    }
    var frequencyData = tpdDTO.frequencyData;
    if (frequencyData != null && frequencyData.endDate != null
        && tpdDTO.startDate.after(frequencyData.endDate)) {
      throw new ConflictingException("Ticket pool definition's startDate after frequency endDate.");
    }
    if (frequencyData != null && frequencyData.endDate != null && frequencyData.startDate != null
        && frequencyData.startDate.after(frequencyData.endDate)) {
      throw new ConflictingException(
          "Ticket pool definition's frequency startDate after frequency endDate.");
    }
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
