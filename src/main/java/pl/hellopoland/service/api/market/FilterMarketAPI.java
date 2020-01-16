package pl.hellopoland.service.api.market;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.FiltersContainerDTO;
import pl.hellopoland.service.FilterService;

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
