package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.Partner;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
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

  @RolesAllowed("admin")
  public PagedCollection<TicketDefinitionDTO> getList(Long partnerId) {
    List<TicketDefinitionDTO> tds =
        service.getTicketDefinitions(partnerId, service.getLoggedUser());
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

  @RolesAllowed("admin")
  public List<TicketDefinitionDTO> update(TicketDefinitionDTO dto) {
    return service.update(dto, service.getLoggedUser());
  }

  @RolesAllowed("admin")
  public void delete(Long id) {
    service.delete(id, service.getLoggedUser());
  }

  @RolesAllowed("admin")
  public TicketDefinitionDTO get(Long id) {
    return service.getTicketDefinition(id, service.getLoggedUser());
  }

}
