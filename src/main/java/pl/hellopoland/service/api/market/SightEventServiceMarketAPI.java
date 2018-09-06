package pl.hellopoland.service.api.market;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightEventServiceMarketAPI {

  @Inject
  SightEventService service;

  @PermitAll
  public PagedCollection getList(SightEventPagedCollectionConfig config) {
    PagedEntityCollection<SightEvent> bos = service.getList(config);
    List<SightEventDTO> dtos =
        bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    service.fetchTicketPoolDefinitions(bos.items, dtos);
    return new PagedCollection(
        dtos.stream().filter(dto -> isAvailable(dto)).collect(Collectors.toList()), bos.config);
  }

  @PermitAll
  public SightEventDTO get(Long id) {
    SightEvent bo = service.get(id);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto));
    return isAvailable(dto) ? dto : null;
  }

  private boolean isAvailable(SightEventDTO dto) {
    List<TicketPoolDefinitionDTO> tpds = dto.ticketPoolDefinitions;
    if (tpds != null && !tpds.isEmpty()) {
      return !tpds.stream().filter(tpd -> tpd.deleted == false)
          .filter(tpd -> new Date().before(tpd.startDate)).collect(Collectors.toList()).isEmpty();
    }
    return false;
  }

}
