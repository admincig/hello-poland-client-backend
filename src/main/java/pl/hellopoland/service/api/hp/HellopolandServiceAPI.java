package pl.hellopoland.service.api.hp;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.service.HellopolandService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class HellopolandServiceAPI {
  @Inject
  private HellopolandService service;

  // @RolesAllowed("admin")
  public PartnerDTO addPartner(PartnerDTO partner) {
    return DtoMapper.getDTO(service.addPartner(partner));
  }
}
