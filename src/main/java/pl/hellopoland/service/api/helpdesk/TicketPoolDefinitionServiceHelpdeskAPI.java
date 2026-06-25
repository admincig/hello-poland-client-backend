package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TicketPoolDefinitionService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TicketPoolDefinitionServiceHelpdeskAPI {

  @Inject
  TicketPoolDefinitionService service;
  @Inject
  PartnerService partnerService;
  @Inject
  SightEventService sightEventService;

  @RolesAllowed({"admin", "salesman"})
  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    Partner partner = sightEventService.get(dto.sightEventId).getPartner();
    return service.add(dto, partner);
  }

  @RolesAllowed({"admin", "salesman"})
  public TicketPoolDefinitionDTO get(Long id, Long partnerId) {
    return service.get(id, getPartner(partnerId));
  }

  @RolesAllowed({"admin", "salesman"})
  public void delete(Long id, Long partnerId) {
    service.delete(id, getPartner(partnerId));
  }

  @RolesAllowed({"admin", "salesman"})
  public TicketPoolDefinitionDTO update(TicketPoolDefinitionDTO dto) {
    return service.update(dto, getPartner(dto.partnerId));
  }

  @RolesAllowed({"admin", "salesman"})
  public List<TicketPoolDefinitionDTO> list(Long partnerId) {
    List<TicketPoolDefinitionDTO> tpds = service.list(partnerId);
    List<Long> partnerHptIds = tpds.stream().map(tpd -> tpd.partnerId).collect(Collectors.toList());
    var partners = partnerService.findByHptIds(partnerHptIds);
    tpds.forEach(tpd -> {
      tpd.partnerId = partners.get(tpd.partnerId).getId();
    });
    return tpds;
  }

  private Partner getPartner(Long partnerId) {
    return partnerId != null ? partnerService.get(partnerId) : null;
  }

}
