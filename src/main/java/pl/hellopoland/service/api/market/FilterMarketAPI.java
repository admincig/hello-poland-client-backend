package pl.hellopoland.service.api.market;

import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.service.FilterService;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

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
