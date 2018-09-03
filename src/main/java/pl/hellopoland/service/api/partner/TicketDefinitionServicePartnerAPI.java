package pl.hellopoland.service.api.partner;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.service.TicketDefinitionService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class TicketDefinitionServicePartnerAPI {

  @Inject
  TicketDefinitionService service;

  @RolesAllowed("partner")
  public TicketDefinitionDTO add(TicketDefinitionDTO dto) {
    return service.add(dto);
  }

  @RolesAllowed("partner")
  public List<TicketDefinitionDTO> getTicketDefinitionsForLoggedUser() {
    return service.getTicketDefinitionsForLoggedUser().stream().map(DtoMapper::getDTO)
        .collect(Collectors.toList());
  }

}
