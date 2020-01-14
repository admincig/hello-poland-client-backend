package pl.hellopoland.service.api.helpdesk;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TicketPoolDefinitionService;

@Stateless
public class TicketPoolDefinitionServiceHelpdeskAPI {

  @Inject
  TicketPoolDefinitionService service;
  @Inject
  PartnerService partnerService;

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
  public List<TicketPoolDefinitionDTO> list(Long partnerId) {
    List<TicketPoolDefinitionDTO> tpds = service.list(partnerId);
    List<Long> partnerHptIds = tpds.stream().map(tpd -> tpd.partnerId).collect(Collectors.toList());
    var partners = partnerService.findByHptIds(partnerHptIds);
    tpds.forEach(tpd -> {
      tpd.partnerId = partners.get(tpd.partnerId).getId();
    });
    return tpds;
  }

}
