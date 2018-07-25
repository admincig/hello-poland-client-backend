package pl.hellopoland.service.api.market;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.ServiceSuperclass;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightEventServiceMarketAPI extends ServiceSuperclass {

  @Inject
  SightEventService service;

  @PermitAll
  public PagedCollection getList(SightEventPagedCollectionConfig config) {
    PagedEntityCollection<SightEvent> bos = service.getList(config);
    List<SightEventDTO> dtos =
        bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    fetchTicketPoolDefinitions(bos.items, dtos);
    return new PagedCollection(dtos, bos.config);
  }

  private void fetchTicketPoolDefinitions(Collection<SightEvent> bos, List<SightEventDTO> dtos) {
    if (hasAnyHptCloudEvent(bos)) {
      var pairedByIds = pairBosWithDtos(bos, dtos);
      var groupedByPartner = groupByPartner(pairedByIds);
      HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
      groupedByPartner.forEach((partner, sightEvents) -> {
        List<TicketPoolDefinitionDTO> poolDefinitions =
            hpt.getTicketPoolDefinitions(partner.getHptToken());
        var poolDefinitionsGroupedBySightEventId =
            poolDefinitions.stream().collect(Collectors.groupingBy(pool -> pool.sightEventId));
        sightEvents.forEach(dto -> {
          dto.ticketPoolDefinitions = poolDefinitionsGroupedBySightEventId.get(dto.sightId);
        });
      });
    } else {
      // TODO other portals
    }
  }

  private Map<Partner, List<SightEventDTO>> groupByPartner(
      List<Pair<SightEvent, SightEventDTO>> groupedById) {
    var groupedByPartner = new HashMap<Partner, List<SightEventDTO>>();
    groupedById.forEach(pair -> {
      Partner partner = pair.getLeft().getPartner();
      if (!groupedByPartner.containsKey(partner)) {
        groupedByPartner.put(partner, new ArrayList<>());
      }
      groupedByPartner.get(partner).add(pair.getRight());
    });
    return groupedByPartner;
  }

  private boolean hasAnyHptCloudEvent(Collection<SightEvent> bos) {
    return bos.stream()
        .anyMatch(se -> se.getPortal().getType().equals(Portal.Type.HELLOTICKET_CLOUD_1));
  }

  private List<Pair<SightEvent, SightEventDTO>> pairBosWithDtos(Collection<SightEvent> bos,
      List<SightEventDTO> dtos) {
    var grouped = new ArrayList<Pair<SightEvent, SightEventDTO>>();
    bos.forEach(bo -> {
      for (var dto : dtos) {
        if (bo.getId().equals(dto.id)) {
          grouped.add(new ImmutablePair<>(bo, dto));
          break;
        }
      }
    });
    return grouped;
  }

  @PermitAll
  public SightEventDTO get(Long id) {
    SightEvent bo = service.get(id);
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

}
