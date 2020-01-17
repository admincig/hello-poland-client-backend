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
    return service.add(dto, service.getLoggedPartner());
  }

  @RolesAllowed("partner")
  public List<TicketDefinitionDTO> getTicketDefinitionsForLoggedUser() {
    return service.getTicketDefinitions(null, service.getLoggedPartner());
  }

  @RolesAllowed("partner")
  public PagedCollection<TicketDefinitionDTO> getList() {
    return new PagedCollection<>(getTicketDefinitionsForLoggedUser(), null);
  }

  @RolesAllowed("partner")
  public List<TicketDefinitionDTO> update(TicketDefinitionDTO dto) {
    Partner partner = service.getLoggedPartner();
    return service.update(dto, partner);
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.delete(id, service.getLoggedPartner());
  }

  @RolesAllowed("partner")
  public TicketDefinitionDTO get(Long id) {
    return service.getTicketDefinition(id, service.getLoggedPartner());
  }

}
