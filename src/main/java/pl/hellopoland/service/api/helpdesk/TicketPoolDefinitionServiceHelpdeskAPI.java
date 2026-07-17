package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.dto.TicketPoolTypeDTO;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.exception.badrequest.BadRequestException;
import pl.hellopoland.service.HelpdeskAccessService;
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
  @Inject
  HelpdeskAccessService accessService;

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    requireNotPromotional(dto);
    accessService.requireSightEventAccess(sightEventService.get(dto.sightEventId));
    Partner partner = sightEventService.get(dto.sightEventId).getPartner();
    return service.add(dto, partner);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public TicketPoolDefinitionDTO get(Long id, Long partnerId) {
    accessService.requirePartnerAccess(getPartner(partnerId));
    return service.get(id, getPartner(partnerId));
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public void delete(Long id, Long partnerId) {
    accessService.requirePartnerAccess(getPartner(partnerId));
    requireNotPromotional(service.get(id, getPartner(partnerId)));
    service.delete(id, getPartner(partnerId));
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public TicketPoolDefinitionDTO update(TicketPoolDefinitionDTO dto) {
    accessService.requirePartnerAccess(getPartner(dto.partnerId));
    requireNotPromotional(service.get(dto.id, getPartner(dto.partnerId)));
    requireNotPromotional(dto);
    return service.update(dto, getPartner(dto.partnerId));
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public List<TicketPoolDefinitionDTO> list(Long partnerId) {
    accessService.requirePartnerAccess(getPartner(partnerId));
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

  private void requireNotPromotional(TicketPoolDefinitionDTO dto) {
    if (dto != null && TicketPoolTypeDTO.PROMOTIONAL.equals(defaultPoolType(dto.poolType))) {
      throw new BadRequestException(
          "Promotional ticket pools are readonly in standard offer management.");
    }
  }

  private TicketPoolTypeDTO defaultPoolType(TicketPoolTypeDTO poolType) {
    return poolType == null ? TicketPoolTypeDTO.STANDARD : poolType;
  }

}
