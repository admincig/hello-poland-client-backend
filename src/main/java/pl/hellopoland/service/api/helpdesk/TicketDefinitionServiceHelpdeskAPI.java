package pl.hellopoland.service.api.helpdesk;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketDefinitionServiceHelpdeskAPI {

  @Inject
  TicketDefinitionService service;
  @Inject
  PartnerService partnerService;

  @RolesAllowed("admin")
  public List<TicketDefinitionDTO> getTicketDefinitions() {
    List<TicketDefinitionDTO> tds = service.getTicketDefinitions(service.getLoggedUser());
    List<Long> partnerHptIds = tds.stream().map(td -> td.partnerId).collect(Collectors.toList());
    var partners = partnerService.findByHptIds(partnerHptIds);
    tds.forEach(td -> {
      td.partnerId = partners.get(td.partnerId).getId();
    });
    return tds;
  }

  @RolesAllowed("admin")
  public PagedCollection getList() {
    return new PagedCollection(getTicketDefinitions(), null);
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
