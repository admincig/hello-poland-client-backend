package pl.hellopoland.service.api.market;

import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.SightService;
import pl.hellopoland.service.TagService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.service.UserService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Stateless
public class SightServiceMarketAPI {

  @Inject
  SightService service;

  @Inject
  SightEventService sightEventService;
  @Inject
  UserService userService;
  @Inject
  TagService tagService;

  @Inject
  private TranslationService translationService;

  @PermitAll
  public PagedCollection<SightDTO> getList(SightPagedCollectionConfig config,
      String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    config.setOrderColumn("e.name");
    config.setOrderDirection("asc");
    PagedEntityCollection<Sight> bos = service.getList(config, language);
    enrichTags(bos.items, language);
    List<SightDTO> dtos = bos.items.stream().map(bo -> {
      var dto = DtoMapper.getDTOWithTags(bo);
      dto.language = bo.getDefaultLanguage().getLanuage();
      return dto;
    }).collect(Collectors.toList());
    if (language != null) {
      dtos.forEach(dto -> dto.language = language.getLanuage());
    }
    return new PagedCollection<>(dtos, bos.config);
  }

  @PermitAll
  public SightDTO get(Long id, String contentLanguageSymbol) {
        LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
        Sight bo = service.get(id);

        if (!bo.isPublished()) {
            return null;
        }

        // tłumaczenie (może podmienić relacje)
        if (language != null) {
            bo = translationService.translateEntity(bo, language);

            var sightEvents = bo.getSightEvents();
            if (sightEvents != null && !sightEvents.isEmpty()) {
                bo.setSightEvents(translationService.translateEntities(sightEvents, language));
            }
        } else {
            language = bo.getDefaultLanguage();
        }

        // >>> KLUCZ: filtrujemy PO tłumaczeniu <<<
        if (bo.getSightEvents() != null) {
            bo.setSightEvents(
                    bo.getSightEvents().stream()
                            .filter(SightEvent::isAccessible)
                            .collect(Collectors.toList())
            );
        } else {
            bo.setSightEvents(Collections.emptyList());
        }

        // categories/tags liczone z przefiltrowanych eventów
        bo.setCategories(bo.getSightEvents().stream()
                .flatMap(se -> se.getCategories().stream())
                .map(SightEventCategory::getCategory)
                .collect(Collectors.toSet()));

        bo.setTags(bo.getSightEvents().stream()
                .flatMap(se -> se.getTags().stream())
                .map(SightEventTag::getTag)
                .collect(Collectors.toSet()));

        // tłumaczenie kategorii/tagów dopiero teraz (na finalnym zbiorze)
        if (language != null) {
            translationService.translateEntities(bo.getCategories(), language);
            translationService.translateEntities(bo.getTags(), language);
        }
        tagService.markPromotionalForActiveCampaigns(bo.getTags());

        var dto = DtoMapper.getFullDTO(bo);
        dto.language = language.getLanuage();

        sightEventService.fetchTicketPoolDefinitions(bo.getSightEvents(), dto.sightEvents, false, true);

        dto.sightEvents = dto.sightEvents.stream()
                .filter(se -> sightEventService.isAvailable(se, null, null))
                .map(se -> {
                    se.ticketPoolDefinitions = null;
                    se.partnerAffiliateCode = null;
                    return se;
                })
                .collect(Collectors.toList());

        var cheapestSE = dto.sightEvents.stream()
                .min(Comparator.comparing(
                    seDto -> seDto.minPrice,
                    Comparator.nullsLast(Comparator.naturalOrder())))
                .orElse(null);

        if (cheapestSE != null && cheapestSE.minPrice != null) {
            dto.minPrice = cheapestSE.minPrice;
            dto.minDiscountPrice = cheapestSE.minDiscountPrice;
        }

        dto.similar = getSimilar(bo, language);

        if (dto.sightEvents != null) {
            dto.sightEvents.forEach(seDto -> seDto.pdfAttachment = null);
        }

        return dto;
  }

  private List<SightDTO> getSimilar(Sight bo, LanguageVersion language) {
    SightPagedCollectionConfig config = prepareConfigForRandom(6);
    config.setPartner(bo.getPartner().getId());
    config.setExcludedIds(Set.of(bo.getId()));
    config.fetchSightEvents(true);
    Collection<Sight> sights = service.getList(config, language).items;
    enrichTags(sights, language);
    fetchSightEventPrices(sights);
    return sights.stream()
        .map(minPriceMapper)
        .collect(Collectors.toList());
  }

  private Function<Sight, SightDTO> minPriceMapper = s -> {
    SightDTO dto = DtoMapper.getDTOWithTags(s);
    if (s.getSightEvents() != null && !s.getSightEvents().isEmpty()) {
      dto.sightEvents =
          s.getSightEvents().stream()
              .map(DtoMapper::getDTO)
              .collect(Collectors.toList());
      dto.sightEvents.stream()
          .min(Comparator.comparing(seDto -> seDto.minPrice,
              Comparator.nullsLast(Comparator.naturalOrder())))
          .ifPresent(cheapest -> {
            dto.minPrice = cheapest.minPrice;
            dto.minDiscountPrice = cheapest.minDiscountPrice;
          });
    }
    return dto;
  };

  @PermitAll
  public PagedCollection<SightDTO> getRecommended(Integer count, LanguageVersion languageVersion) {
    SightPagedCollectionConfig config = prepareConfigForRandom(count);
    PagedEntityCollection<Sight> pagedCollection = service.getList(config, languageVersion);
    enrichTags(pagedCollection.items, languageVersion);
    fetchSightEventPrices(pagedCollection.items);
    return new PagedCollection<>(
        pagedCollection.items.stream()
            .map(minPriceMapper)
            .collect(Collectors.toList()),
        pagedCollection.config);
  }

  private void fetchSightEventPrices(Collection<Sight> items) {
    List<SightEvent> sightEvents = new ArrayList<>();
    for (Sight s : items) {
      sightEvents.addAll(s.getSightEvents());
    }
    HelloTicket hptClient =
        new HelloTicket(sightEventService.getPortal("Hello Ticket Cloud").getUrl());
    Map<Long, List<SightEvent>> grouped =
        hptClient.getSightEventsInDateRange(sightEvents, null, null).stream()
            .collect(Collectors.groupingBy(se -> se.getSight().getId()));

    List<SightEvent> availableSightEvents = grouped.values().stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    List<pl.hellopoland.dto.SightEventDTO> sightEventDtos = availableSightEvents.stream()
        .map(DtoMapper::getDTO)
        .collect(Collectors.toList());
    sightEventService.fetchTicketPoolDefinitions(availableSightEvents, sightEventDtos, false, true);
    Map<Long, pl.hellopoland.dto.SightEventDTO> sightEventDtosById = sightEventDtos.stream()
        .collect(Collectors.toMap(dto -> dto.id, dto -> dto));

    availableSightEvents.forEach(sightEvent -> {
      pl.hellopoland.dto.SightEventDTO dto = sightEventDtosById.get(sightEvent.getId());
      if (dto != null) {
        sightEvent.setMinPrice(dto.minPrice);
        sightEvent.setMinDiscountPrice(dto.minDiscountPrice);
      }
    });

    for (Sight s : items) {
      s.setSightEvents(grouped.get(s.getId()));
    }
  }

  private void enrichTags(Collection<Sight> sights, LanguageVersion language) {
    Map<Sight, Set<Tag>> tagsBySight = tagService.getForSights(sights).stream()
        .filter(relation -> relation.getSightEvent().isAccessible())
        .collect(Collectors.groupingBy(
            relation -> relation.getSightEvent().getSight(),
            Collectors.mapping(SightEventTag::getTag, Collectors.toSet())));
    Set<Tag> tags = tagsBySight.values().stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
    if (language != null) {
      translationService.translateEntities(tags, language);
    }
    tagService.markPromotionalForActiveCampaigns(tags);
    sights.forEach(sight -> sight.setTags(
        tagsBySight.getOrDefault(sight, Collections.emptySet())));
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

  @RolesAllowed("user")
  public SightDTO addFavourite(Long id) {
    Sight bo = service.get(id);
    bo.addUser(userService.getLoggedUser());
    bo.setFavourite(true);
    return DtoMapper.getDTO(bo);
  }

  @RolesAllowed("user")
  public void removeFavourite(Long id) {
    Sight bo = service.get(id);
    if (!bo.getUsers().contains(userService.getLoggedUser())) {
      throw new ConflictingException("Sight [id:" + id + "] not in favourites");
    }
    bo.removeUser(userService.getLoggedUser());
  }

    @RolesAllowed("user")
    public PagedCollection<SightDTO> favourites(SightPagedCollectionConfig config,
                                                String contentLanguageSymbol) {

        if (userService.getLoggedUser() != null) {
            config.onlyFavourite(userService.getLoggedUser().getId());
        }

        LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);

        // żeby dało się policzyć minPrice/minDiscountPrice:
        config.fetchSightEvents(true);
        config.setOrderColumn("e.name");
        config.setOrderDirection("asc");

        PagedEntityCollection<Sight> bos = service.getList(config, language);
        enrichTags(bos.items, language);

        // dociąga ceny z HelloTicket i podstawia je do sightEvents
        fetchSightEventPrices(bos.items);

        List<SightDTO> dtos = bos.items.stream()
                .map(minPriceMapper)   // ten mapper ustawia dto.minPrice / dto.minDiscountPrice
                .collect(Collectors.toList());

        if (language != null) {
            dtos.forEach(dto -> dto.language = language.getLanuage());
        }

        return new PagedCollection<>(dtos, bos.config);
    }

}
