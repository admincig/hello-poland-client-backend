package pl.hellopoland.service.api.market;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.config.TagPagedCollectionConfig;
import pl.hellopoland.dto.FilterDTO;
import pl.hellopoland.dto.FilterPriceEntryDTO;
import pl.hellopoland.dto.SearchResultDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.service.CategoryService;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TagService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

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
  public SearchResultDTO search(LanguageVersion languageVersion, String query, Long[] categoryIds,
      Long[] tagIds, String city, Date fromDate, Date toDate, Integer minPrice, Integer maxPrice) {

    List<SightEvent> ses =
        seService.getList(languageVersion, query, categoryIds, tagIds, city,
            fromDate, toDate, minPrice, maxPrice, null);

    Map<Sight, List<SightEvent>> ss =
        ses.stream().collect(groupingBy(SightEvent::getSight));

    SearchResultDTO oro = new SearchResultDTO();
    oro.sights = ss.entrySet().stream().map(entry -> {
      Sight s = tService.translateEntity(entry.getKey(), languageVersion);
      List<SightEvent> se = entry.getValue();
      List<Category> categories = getCategories(se);
      List<Tag> tags = getTags(se);

      SightDTO dto = DtoMapper.getDTO(s);
      dto.sightEvents = se.stream()
          .map(DtoMapper::getDTO)
          .collect(toList());
      dto.minPrice = dto.sightEvents.stream()
          .map(sedto -> sedto.minPrice)
          .filter(Objects::nonNull)
          .min(Comparator.naturalOrder())
          .orElse(null);
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

  private List<Tag> getTags(List<SightEvent> ses) {
    return ses.stream()
        .filter(event -> event.getTags() != null)
        .flatMap(event -> event.getTags().stream())
        .map(SightEventTag::getTag)
        .distinct()
        .collect(toList());
  }

  private List<Category> getCategories(List<SightEvent> ses) {
    List<Category> categories = ses.stream()
        .filter(event -> event.getCategories() != null)
        .flatMap(event -> event.getCategories().stream())
        .map(SightEventCategory::getCategory)
        .distinct()
        .collect(toList());
    return categories;
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
