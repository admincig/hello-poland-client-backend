package pl.hellopoland.service.api.market;

import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.service.FilterService;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class FilterMarketAPI {

  @Inject
  private FilterService service;

  @PermitAll
  public FiltersContainerDTO getForSightEvents() {
    return service.getForSightEvents(null);
  }

  @PermitAll
  public FiltersContainerDTO getForSights() {
    return service.getForSights();
  }

}
