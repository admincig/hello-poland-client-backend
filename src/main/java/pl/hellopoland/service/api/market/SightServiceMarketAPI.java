package pl.hellopoland.service.api.market;

import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightServiceMarketAPI {

  @Inject
  SightService service;

  @PermitAll
  public PagedCollection getList(SightPagedCollectionConfig config) {
    PagedEntityCollection<Sight> bos = service.getList(config);
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public SightDTO get(Long id) {
    Sight bo = service.get(id);
    bo.setSightEvents(
        bo.getSightEvents().stream().filter(se -> se.isActive()).collect(Collectors.toList()));
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

}
