package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.Partner;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.HelpdeskAccessService;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TicketDefinitionService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.lang.System.Logger;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TicketDefinitionServiceHelpdeskAPI {

  final static Logger logger = System.getLogger(TicketDefinitionServiceHelpdeskAPI.class.getName());
  @Inject
  TicketDefinitionService service;
  @Inject
  PartnerService partnerService;
  @Inject
  HelpdeskAccessService accessService;

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public TicketDefinitionDTO add(TicketDefinitionDTO dto) {
    accessService.requirePartnerAccess(getPartner(dto.partnerId));
    return service.add(dto, getSubject(dto.partnerId));
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public PagedCollection<TicketDefinitionDTO> getList(Long partnerId) {
    accessService.requirePartnerAccess(getPartner(partnerId));
    List<TicketDefinitionDTO> tds =
        service.getTicketDefinitions(partnerId, getSubject(partnerId));
    List<Long> partnerHptIds = tds.stream().map(td -> td.partnerId).collect(Collectors.toList());
    var partners = partnerService.findByHptIds(partnerHptIds);
    for (var iter = tds.iterator(); iter.hasNext();) {
      TicketDefinitionDTO td = iter.next();
      Partner tdPartner = partners.get(td.partnerId);
      if (tdPartner == null) {
        iter.remove();
      } else {
        td.partnerId = tdPartner.getId();
      }
    }
    return new PagedCollection<>(tds, null);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public List<TicketDefinitionDTO> update(TicketDefinitionDTO dto) {
    accessService.requirePartnerAccess(getPartner(dto.partnerId));
    return service.update(dto, getSubject(dto.partnerId));
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager"})
  public void delete(Long id, Long partnerId) {
    accessService.requirePartnerAccess(getPartner(partnerId));
    service.delete(id, getSubject(partnerId));
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public TicketDefinitionDTO get(Long id, Long partnerId) {
    accessService.requirePartnerAccess(getPartner(partnerId));
    return service.getTicketDefinition(id, getSubject(partnerId));
  }

  private Partner getPartner(Long partnerId) {
    return partnerId != null ? partnerService.get(partnerId) : null;
  }

  private pl.hellopoland.bo.HptSubject getSubject(Long partnerId) {
    return partnerId != null ? partnerService.get(partnerId) : service.getHelpdeskHptSubject();
  }

}
