package pl.hellopoland.service.api.market;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.SightService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightServiceMarketAPI {

  @Inject
  SightService service;

  @Inject
  SightEventService sightEventService;

  @Inject
  private TranslationService translationService;

  @PermitAll
  public PagedCollection getList(SightPagedCollectionConfig config, String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    config.setOrderColumn("e.name");
    config.setOrderDirection("asc");
    PagedEntityCollection<Sight> bos = service.getList(config, language);
    var dtos = bos.items.stream().map(bo -> {
      var dto = DtoMapper.getDTO(bo);
      dto.language = bo.getDefaultLanguage().getLanuage();
      return dto;
    }).collect(Collectors.toList());
    if (language != null) {
      dtos.forEach(dto -> dto.language = language.getLanuage());
    }
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public SightDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    Sight bo = service.get(id);
    if (bo.isPublished()) {
      bo.setSightEvents(bo.getSightEvents().stream()
          .filter(SightEvent::isAccessible)
          .collect(Collectors.toList()));
      bo.setCategories(bo.getSightEvents().stream().flatMap(se -> se.getCategories().stream())
          .map(SightEventCategory::getCategory).collect(Collectors.toSet()));
      if (language != null) {
        bo = translationService.translateEntity(bo, language, true);
        translationService.translateEntities(bo.getCategories(), language, false);
        var sightEvents = bo.getSightEvents();
        if (sightEvents != null && !sightEvents.isEmpty()) {
          bo.setSightEvents(translationService.translateEntities(sightEvents, language, true));
        }
      } else {
        language = bo.getDefaultLanguage();
      }
      var dto = DtoMapper.getFullDTO(bo);
      dto.language = language.getLanuage();
      sightEventService.fetchTicketPoolDefinitions(bo.getSightEvents(), dto.sightEvents, false);
      dto.sightEvents = dto.sightEvents.stream()
          .filter(se -> sightEventService.isAvailable(se, null, null)).map(se -> {
            se.ticketPoolDefinitions = null;
            se.partnerAffiliateCode = null;
            return se;
          }).collect(Collectors.toList());
      dto.minPrice = dto.sightEvents.stream().min(Comparator.comparing(seDto -> seDto.minPrice))
          .map(seDto -> seDto.minPrice).orElse(null);
      dto.similar = getSimilar(bo, language);
      return dto;
    }
    return null;
  }

  private List<SightDTO> getSimilar(Sight bo, LanguageVersion language) {
    SightPagedCollectionConfig config = prepareConfigForRandom(6);
    config.setPartner(bo.getPartner().getId());
    config.setExcludedIds(Set.of(bo.getId()));
    HelloTicket hptClient =
        new HelloTicket(sightEventService.getPortal("Hello Ticket Cloud").getUrl());
    Collection<Sight> sights = service.getList(config, language).items;
    hptClient
        .getSightEventsInDateRange(
            new ArrayList<SightEvent>(sights.stream()
                .flatMap(sight -> sight.getSightEvents().stream()).collect(Collectors.toList())),
            null, null);
    return sights.stream().map(minPriceMapper)
        .collect(Collectors.toList());
  }

  private Function<Sight, SightDTO> minPriceMapper = s -> {
    SightDTO dto = DtoMapper.getDTO(s);
    dto.sightEvents =
        s.getSightEvents().stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    dto.minPrice = dto.sightEvents.stream().min(Comparator.comparing(seDto -> seDto.minPrice))
        .map(seDto -> seDto.minPrice).orElse(null);
    return dto;
  };

  @PermitAll
  public PagedCollection getRecommended(Integer count, LanguageVersion languageVersion) {
    SightPagedCollectionConfig config = prepareConfigForRandom(count);
    PagedEntityCollection<Sight> pagedCollection = service.getList(config, languageVersion);
    HelloTicket hptClient =
        new HelloTicket(sightEventService.getPortal("Hello Ticket Cloud").getUrl());
    Collection<Sight> sights = service.getList(config, languageVersion).items;
    hptClient
        .getSightEventsInDateRange(
            new ArrayList<SightEvent>(sights.stream()
                .flatMap(sight -> sight.getSightEvents().stream()).collect(Collectors.toList())),
            null, null);
    return new PagedCollection(
        pagedCollection.items.stream().map(minPriceMapper).collect(Collectors.toList()),
        pagedCollection.config);
  }

  private SightPagedCollectionConfig prepareConfigForRandom(Integer count) {
    SightPagedCollectionConfig config = new SightPagedCollectionConfig();
    config.setPageSize(count);
    config.setOrderColumn("random()");
    config.onlyActive();
    config.onlyPublished();
    config.fetchSightEvents(true);
    return config;
  }

}
