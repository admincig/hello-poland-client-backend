package pl.hellopoland.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.HptSubject;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.exception.badrequest.BadRequestException;
import pl.hellopoland.util.HelloTicket;

@Stateless
public class TicketDefinitionService extends ServiceSuperclass {

  @Inject
  PartnerService partnerService;
  @Inject
  SightEventService sightEventService;

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

  public List<TicketDefinitionDTO> update(TicketDefinitionDTO dto,
      HptSubject hptSubject) {
    if (dto.price < 0) {
      throw new BadRequestException("The ticket price must be greater than 0");
    }
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
