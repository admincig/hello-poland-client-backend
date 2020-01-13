package pl.hellopoland.service.api.helpdesk;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketDefinitionServiceHelpdeskAPI {

  @Inject
  TicketDefinitionService service;

  @RolesAllowed("admin")
  public List<TicketDefinitionDTO> getTicketDefinitionsForLoggedUser() {
    return service.getTicketDefinitions(service.getLoggedPartner());
  }

  @RolesAllowed("admin")
  public PagedCollection getList() {
    return new PagedCollection(getTicketDefinitionsForLoggedUser(), null);
  }

  @RolesAllowed("admin")
  public List<TicketDefinitionDTO> update(TicketDefinitionDTO dto) {
    TicketDefinition td = service.findByExternalId(dto.id);
    return service.update(td, dto, service.getLoggedPartner());
  }

  @RolesAllowed("admin")
  public void delete(Long id) {
    service.delete(id, service.getLoggedPartner());
  }

  @RolesAllowed("admin")
  public TicketDefinitionDTO get(Long id) {
    return service.getTicketDefinition(id, service.getLoggedPartner());
  }

}
