package pl.hellopoland.service.api.partner;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightEventServicePartnerAPI {

  @Inject
  SightEventService service;

  @RolesAllowed("partner")
  public PagedCollection getList(SightEventPagedCollectionConfig config) {
    config.onlyCurrentPartner(true);
    config.onlyActive();
    PagedEntityCollection<SightEvent> bos = service.getList(config);
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    service.fetchTicketPoolDefinitions(bos.items, dtos);
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("partner")
  public SightEventDTO create(SightEventDTO dto) {
    SightEvent bo = service.create(dto, null);
    dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightEventDTO update(SightEventDTO dto) {
    SightEvent bo = service.updateForLoggedUser(dto);
    dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public SightEventDTO get(Long id) {
    SightEvent bo = service.getForLoggedUser(id);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto));
    return dto;
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.deleteForLoggedUser(id);
  }

  @RolesAllowed("partner")
  public SightEventDTO uploadMainImage(Long id, byte[] icon) {
    SightEvent bo = service.uploadMainImageForLoggedUser(id, icon);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto));
    return dto;
  }
}
