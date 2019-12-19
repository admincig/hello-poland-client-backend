package pl.hellopoland.service.api.partner;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.TicketDefinitionService;

@Stateless
public class TicketDefinitionServicePartnerAPI {

  @Inject
  TicketDefinitionService service;

  @RolesAllowed("partner")
  public TicketDefinitionDTO add(TicketDefinitionDTO dto, Partner partner) {
    return service.add(dto, null);
  }

  @RolesAllowed("partner")
  public List<TicketDefinitionDTO> getTicketDefinitionsForLoggedUser() {
    return service.getTicketDefinitionsForLoggedUser();
  }

  @RolesAllowed("partner")
  public PagedCollection getList() {
    return new PagedCollection(service.getTicketDefinitionsForLoggedUser(), null);
  }

  @RolesAllowed("partner")
  public List<TicketDefinitionDTO> update(TicketDefinitionDTO dto) {
    return service.update(dto, null);
  }

}
