package pl.hellopoland.service.api.partner;

import java.util.List;
import java.util.stream.Collectors;
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
  public AgreementDTO getForPartner(Long id) {
    return DtoMapper.getDTO(service.getForLoggedUser(id));
  }

  @RolesAllowed("partner")
  public List<AgreementDTO> getForPartner() {
    return service.getForPartner().stream().map(a -> DtoMapper.getDTO(a))
        .collect(Collectors.toList());
  }

  @RolesAllowed("partner")
  public AgreementDTO create(AgreementDTO dto) {
    return DtoMapper.getDTO(service.create(dto));
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.deleteForLoggedUser(id);
  }

  public AgreementDTO update(AgreementDTO dto) {
    return DtoMapper.getDTO(service.updateForLoggedUser(dto));
  }

}
