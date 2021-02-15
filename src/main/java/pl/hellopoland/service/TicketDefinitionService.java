package pl.hellopoland.service;

import pl.hellopoland.bo.HptSubject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.exception.badrequest.BadRequestException;
import pl.hellopoland.util.HelloTicket;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TicketDefinitionService extends ServiceSuperclass {

  @Inject
  PartnerService partnerService;
  @Inject
  SightEventService sightEventService;

  public List<TicketDefinitionDTO> getTicketDefinitions(Long partnerId, HptSubject hptSubject) {
    Portal portal = getPortal("Hello Ticket Cloud");
    HelloTicket hpt = new HelloTicket(portal.getUrl());
    List<TicketDefinitionDTO> tds = hpt.getTicketDefinitions(hptSubject.getHptToken());
    if (partnerId != null) {
      Partner partner = partnerService.get(partnerId);
      tds = tds.stream().filter(td -> td.partnerId.equals(partner.getHptId()))
          .collect(Collectors.toList());
    }
    return tds;
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
