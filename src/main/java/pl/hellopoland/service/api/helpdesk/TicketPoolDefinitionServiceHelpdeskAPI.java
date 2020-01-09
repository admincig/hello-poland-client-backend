package pl.hellopoland.service.api.helpdesk;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.TicketPoolDefinitionService;

@Stateless
public class TicketPoolDefinitionServiceHelpdeskAPI {

  @Inject
  TicketPoolDefinitionService service;

  @RolesAllowed("admin")
  public TicketPoolDefinitionDTO add(TicketPoolDefinitionDTO dto) {
    return service.add(dto);
  }

  @RolesAllowed("admin")
  public TicketPoolDefinitionDTO get(Long id) {
    return service.get(id);
  }

  @RolesAllowed("admin")
  public void delete(Long id) {
    service.delete(id);
  }

  @RolesAllowed("admin")
  public List<TicketPoolDefinitionDTO> update(TicketPoolDefinitionDTO dto) {
    return service.update(dto);
  }

  @RolesAllowed("admin")
  public List<TicketPoolDefinitionDTO> list() {
    return service.list();
  }

}
