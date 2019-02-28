package pl.hellopoland.service.api.market;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.service.FilterService;

@Stateless
public class FilterMarketAPI {

  @Inject
  SightEventServiceMarketAPI sightEventAPI;
  @Inject
  private FilterService service;

  @PermitAll
  public FiltersContainerDTO getForSightEvents() {
    return service.getForSightEvents(null);
  }

  @SuppressWarnings("unchecked")
  @PermitAll
  public FiltersContainerDTO getForAvailableSightEvents() {
    var config = new SightEventPagedCollectionConfig();
    config.onlyActive();
    config.onlyPublished();
    var sightEvents =
        (Collection<SightEventDTO>) sightEventAPI.getList(config, null, null, null).items;
    return service
        .getForSightEvents(sightEvents.stream().map(se -> se.id).collect(Collectors.toSet()));
  }

  @PermitAll
  public FiltersContainerDTO getForSights() {
    return service.getForSights();
  }

}
