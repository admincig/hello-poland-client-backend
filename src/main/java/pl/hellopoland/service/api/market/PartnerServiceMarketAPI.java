package pl.hellopoland.service.api.market;

import pl.hellopoland.bo.Address;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TagService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class PartnerServiceMarketAPI {

  @Inject
  PartnerService service;
  @Inject
  TranslationService transService;
  @Inject
  TagService tagService;

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
    bo.setTags(transService.translateEntities(bo.getTags(), parseLang));
    tagService.markPromotionalForActiveCampaigns(bo.getTags());
    Map<Sight, Set<Tag>> tagsBySight = bo.getSightEvents().stream()
        .filter(sightEvent -> sightEvent.isAccessible())
        .flatMap(sightEvent -> sightEvent.getTags().stream())
        .collect(Collectors.groupingBy(
            relation -> relation.getSightEvent().getSight(),
            Collectors.mapping(SightEventTag::getTag, Collectors.toSet())));
    bo.getSight().forEach(sight -> sight.setTags(
        tagsBySight.getOrDefault(sight, Collections.emptySet())));
    return DtoMapper.getFullMarketPartnerDTO(bo);
  }
}
