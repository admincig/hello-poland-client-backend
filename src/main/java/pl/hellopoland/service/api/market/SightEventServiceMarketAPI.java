package pl.hellopoland.service.api.market;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketPoolDTO;
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
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public SightEventDTO get(Long id) {
    SightEvent bo = service.get(id);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto));
    return dto;
  }

  @PermitAll
  public TicketPoolDTO checkAvailability(Long ticketPoolDefinitionId) {
    return service.checkAvailability(ticketPoolDefinitionId);
  }

}
