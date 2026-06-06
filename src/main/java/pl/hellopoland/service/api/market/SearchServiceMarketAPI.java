package pl.hellopoland.service.api.market;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.hellopoland.bo.*;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.config.TagPagedCollectionConfig;
import pl.hellopoland.dto.FilterDTO;
import pl.hellopoland.dto.FilterPriceEntryDTO;
import pl.hellopoland.dto.SearchResultDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.service.*;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Stateless
public class SearchServiceMarketAPI {

  @Inject
  SightEventService seService;
  @Inject
  TranslationService tService;
  @Inject
  CategoryService catService;
  @Inject
  TagService tagService;
  @Inject
  UserService userService;
  @Inject
  SightService sightService;

  private List<FilterPriceEntryDTO> predefinedPriceFilters;

  public SearchServiceMarketAPI() {
    predefinedPriceFilters = new ArrayList<>();
    predefinedPriceFilters.add(new FilterPriceEntryDTO(0, 2000));
    predefinedPriceFilters.add(new FilterPriceEntryDTO(2000, 4000));
    predefinedPriceFilters.add(new FilterPriceEntryDTO(4000, 6000));
    predefinedPriceFilters.add(new FilterPriceEntryDTO(6000, 8000));
    predefinedPriceFilters.add(new FilterPriceEntryDTO(8000, null));
  }

  @PermitAll
  public SearchResultDTO search(SightEventPagedCollectionConfig seConfig, Integer minPrice,
      Integer maxPrice) {
    seConfig.onlyAvailable();
    seConfig.onlyActive();
    seConfig.onlyPublished();
    seConfig.setOrderColumn("random()");
    seConfig.setFetchCategories(true);
    seConfig.setFetchTags(true);
    PagedEntityCollection<SightEvent> bos = seService.getList(seConfig);
    List<SightEvent> ses = bos.items.stream()
        .filter(SightEvent::isAccessible)
        .collect(Collectors.toList());

    if (minPrice != null) {
      ses = ses.stream()
          .filter(se -> se.getMinPrice() != null && se.getMinPrice() >= minPrice)
          .collect(toList());
    }
    if (maxPrice != null) {
      ses = ses.stream()
          .filter(se -> se.getMinPrice() != null && se.getMinPrice() <= maxPrice)
          .collect(toList());
    }

    Map<Sight, List<SightEvent>> ss =
        ses.stream().collect(groupingBy(SightEvent::getSight));

    List<Long> favouriteSights = List.of();
    User loggedUser = userService.getLoggedUser();
    if (loggedUser != null) {
      favouriteSights = sightService.getAllFavouritesIdsForLoggedUser(loggedUser);
    }
    LanguageVersion languageVersion = seConfig.getLanguage();
    SearchResultDTO oro = new SearchResultDTO();
    List<Long> finalFavouriteSights = favouriteSights;
    oro.sights = ss.entrySet().stream().map(entry -> {
      Sight s = tService.translateEntity(entry.getKey(), languageVersion);
      List<SightEvent> se = entry.getValue();
      List<Category> categories = getCategories(se);
      List<Tag> tags = getTags(se);

      SightDTO dto = DtoMapper.getDTO(s);
      dto.favourite = finalFavouriteSights.contains(s.getId());
      dto.sightEvents = se.stream()
          .map(DtoMapper::getDTO)
          .collect(toList());
      findCheapestPricedSightEvent(dto.sightEvents).ifPresent(cheapest -> {
        dto.minPrice = cheapest.minPrice;
        dto.minDiscountPrice = cheapest.minDiscountPrice;
      });
      dto.categories = tService.translateEntities(categories, languageVersion).stream()
          .map(DtoMapper::getDTO)
          .collect(toSet());
      dto.tags = tService.translateEntities(tags, languageVersion).stream()
          .map(DtoMapper::getDTO)
          .collect(toSet());
      return dto;
    }).collect(toList());
    return oro;
  }

  Optional<SightEventDTO> findCheapestPricedSightEvent(List<SightEventDTO> sightEvents) {
    if (sightEvents == null) {
      return Optional.empty();
    }
    return sightEvents.stream()
        .filter(sightEvent -> sightEvent.minPrice != null)
        .min(Comparator.comparing(sightEvent -> sightEvent.minPrice));
  }

  private List<Tag> getTags(List<SightEvent> ses) {
    return ses.stream()
        .filter(event -> event.getTags() != null)
        .flatMap(event -> event.getTags().stream())
        .map(SightEventTag::getTag)
        .distinct()
        .collect(toList());
  }

  private List<Category> getCategories(List<SightEvent> ses) {
    return ses.stream()
        .filter(event -> event.getCategories() != null)
        .flatMap(event -> event.getCategories().stream())
        .map(SightEventCategory::getCategory)
        .distinct()
        .collect(toList());
  }

  @PermitAll
  public FilterDTO filters(LanguageVersion languageVersion) {
    FilterDTO filter = new FilterDTO();
    filter.prices = predefinedPriceFilters;
    filter.city = seService.getCitiesForPublicEvents();
    Collection<Category> categories =
        catService.pagedList(new CategoryPagedCollectionConfig()).items;
    categories = tService.translateEntities(categories, languageVersion);
    filter.categories = categories.stream().map(DtoMapper::getDTO).collect(toList());
    Collection<Tag> tags = tagService.pagedList(new TagPagedCollectionConfig()).items;
    tags = tService.translateEntities(tags, languageVersion);
    filter.tags = tags.stream().map(DtoMapper::getDTO).collect(toList());
    return filter;
  }

}
