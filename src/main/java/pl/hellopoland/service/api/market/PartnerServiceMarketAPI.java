package pl.hellopoland.service.api.market;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class PartnerServiceMarketAPI {

  @Inject
  PartnerService service;
  @Inject
  TranslationService transService;

  @PermitAll
  public PagedCollection list(LanguageVersion languageVersion) {
    PartnerPagedCollectionConfig config = new PartnerPagedCollectionConfig();
    PagedEntityCollection<Partner> pec = service.getList(config);
    pec.items = transService.translateEntities(pec.items, languageVersion, false);
    List<MarketPartnerDTO> dtos =
        pec.items.stream().map(DtoMapper::getMarketDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, config);
  }

  @PermitAll
  public MarketPartnerDTO get(Long id, LanguageVersion parseLang) {
    Partner bo = service.getPartnerWithCategoriesAndCities(id);
    bo = transService.translateEntity(bo, parseLang, true);
    return DtoMapper.getFullMarketPartnerDTO(bo);
  }
}
