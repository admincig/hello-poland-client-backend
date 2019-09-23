package pl.hellopoland.service.api.market;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class PartnerServiceMarketAPI {

  @Inject
  PartnerService service;

  @PermitAll
  public PagedCollection list() {
    PartnerPagedCollectionConfig config = new PartnerPagedCollectionConfig();
    PagedEntityCollection<Partner> pec = service.getList(config);
    List<MarketPartnerDTO> dtos =
        pec.items.stream().map(DtoMapper::getMarketDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, config);
  }
}
