package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.service.AgreementService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class AgreementServicePartnerAPI {

  @Inject
  AgreementService service;

  @RolesAllowed("partner")
  public AgreementDTO getForLoggedUser(Long id) {
    return DtoMapper.getDTO(service.getForLoggedUser(id));
  }

  @RolesAllowed("partner")
  public AgreementDTO create(AgreementDTO dto) {
    return DtoMapper.getDTO(service.create(dto));
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.deleteForLoggedUser(id);
  }

}
