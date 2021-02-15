package pl.hellopoland.service.api.market;

import pl.hellopoland.bo.Address;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class PartnerServiceMarketAPI {

  @Inject
  PartnerService service;
  @Inject
  TranslationService transService;

  @PermitAll
  public PagedCollection<MarketPartnerDTO> list(LanguageVersion languageVersion) {
    PartnerPagedCollectionConfig config = new PartnerPagedCollectionConfig();
    config.setBlocked(false);
    PagedEntityCollection<Partner> pec = service.getList(config);
    pec.items = transService.translateEntities(pec.items, languageVersion);
    List<MarketPartnerDTO> dtos =
        pec.items.stream().map(DtoMapper::getMarketDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, config);
  }

  @PermitAll
  public MarketPartnerDTO get(Long id, LanguageVersion parseLang) {
    Partner bo = service.getPartnerWithCategoriesAndTagsAndCities(id);
    Address address = transService.translateEntity(bo.getAddress(), parseLang);
    bo = transService.translateEntity(bo, parseLang);
    bo.setAddress(address);
    bo.setCategories(transService.translateEntities(bo.getCategories(), parseLang));
    bo.setSight(transService.translateEntities(bo.getSight(), parseLang));
    bo.setSightEvents(transService.translateEntities(bo.getSightEvents(), parseLang));
    return DtoMapper.getFullMarketPartnerDTO(bo);
  }
}
