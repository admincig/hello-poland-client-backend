package pl.hellopoland.service.api.partner;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.enums.LanguageVersion;
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
    return service.getForLoggedUser().stream().map(a -> DtoMapper.getDTO(a))
        .collect(Collectors.toList());
  }

  @RolesAllowed("partner")
  public AgreementDTO create(AgreementDTO dto) {
    return DtoMapper.getDTO(service.create(dto));
  }

  @RolesAllowed("partner")
  public AgreementDTO createLanguageVersion(AgreementDTO dto, LanguageVersion languge) {
    return DtoMapper.getDTO(service.createLanguageVersion(dto, languge));
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.deleteForLoggedUser(id);
  }

  @RolesAllowed("partner")
  public AgreementDTO update(AgreementDTO dto) {
    return DtoMapper.getDTO(service.updateForLoggedUser(dto));
  }

  @RolesAllowed("partner")
  public AgreementDTO updateLanguageVersion(AgreementDTO dto, String language) {
    return DtoMapper.getDTO(service.updateLanguageVersion(dto, language));
  }

}
