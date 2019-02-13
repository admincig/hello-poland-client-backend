package pl.hellopoland.service.api.market;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.rest.dto.AvailableTicketNumberAssociationORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TicketPoolDefinitionService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightEventServiceMarketAPI {

  @Inject
  SightEventService service;

  @Inject
  TicketPoolDefinitionService tpdService;

  @Inject
  private TranslationService translationService;

  @PermitAll
  public PagedCollection getList(SightEventPagedCollectionConfig config, Date fromDate, Date toDate,
      String language) {
    config.setOrderColumn("name");
    config.setOrderDirection("asc");
    PagedEntityCollection<SightEvent> bos = service.getList(config);
    if (language != null && !language.toLowerCase().contains("pl")) {
      bos.items = translationService.translateEntities(bos.items, language, false);
    }
    List<SightEventDTO> dtos =
        bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    service.fetchTicketPoolDefinitions(bos.items, dtos, false);
    List<SightEventDTO> list =
        dtos.stream().filter(dto -> service.isAvailable(dto, fromDate, toDate)).map(dto -> {
          dto.ticketPoolDefinitions = null;
          return dto;
        }).collect(Collectors.toList());
    return new PagedCollection(list, bos.config);
  }

  @PermitAll
  public SightEventDTO get(Long id, String language) {
    SightEvent bo = service.get(id);
    if (bo.isPublished()) {
      if (language != null && !language.toLowerCase().contains("pl")) {
        bo = translationService.translateEntity(bo, language, true);
        var agreements = bo.getAgreements();
        var tickets = bo.getTickets();
        if (agreements != null && !agreements.isEmpty()) {
          bo.setAgreements(
              Set.copyOf(translationService.translateEntities(agreements, language, true)));
        }
        if (tickets != null && !tickets.isEmpty()) {
          bo.setTickets(translationService.translateEntities(tickets, language, true));
        }
      }
      var dto = DtoMapper.getFullDTO(bo);
      service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), false);
      return service.isAvailable(dto, null, null) ? dto : null;
    }
    return null;
  }

  @PermitAll
  public AvailableTicketNumberAssociationORO checkAvailability(Long sightEventId, Date fromDate,
      Date toDate) {
    return new AvailableTicketNumberAssociationORO(
        service.checkAvailability(sightEventId, fromDate, toDate));
  }

}
