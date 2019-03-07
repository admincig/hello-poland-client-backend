package pl.hellopoland.service.api.partner;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightService;
import pl.hellopoland.util.DtoMapper;

@Stateless
public class SightServicePartnerAPI {

  @Inject
  SightService service;

  @RolesAllowed("partner")
  public SightDTO create(SightDTO dto) {
    Sight bo = service.create(dto, null);
    dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightDTO createLanguageVesrion(SightDTO dto, LanguageVersion language) {
    return DtoMapper.getFullDTO(service.createLanguageVesrion(dto, language));
  }

  @RolesAllowed("partner")
  public PagedCollection getList() {
    List<Sight> bos = service.getActiveForPartner();
    var dtos = bos.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, null);
  }

  @RolesAllowed("partner")
  public SightDTO get(Long id) {
    Sight bo = service.getActiveForLoggedUser(id);
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightDTO update(SightDTO dto) {
    Sight bo = service.updateForLoggedUser(dto);
    dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightDTO updateLanguageVersion(SightDTO dto, LanguageVersion language) {
    return DtoMapper.getFullDTO(service.updateLanguageVersionForLoggedUser(dto, language));
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.deleteForLoggedUser(id);
  }

  @RolesAllowed("partner")
  public SightDTO uploadMainImage(Long id, byte[] icon) {
    Sight bo = service.uploadMainImageForLoggedUser(id, icon);
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightDTO uploadImage(Long id, byte[] icon) {
    Sight bo = service.addImageToSightGallery(id, icon);
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightDTO removeImageFromGallery(Long id, Long imgId) {
    Sight bo = service.removeImageFromGallery(id, imgId);
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    Sight bo = service.changeDefaultLanguage(id, language);
    SightDTO dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

}
