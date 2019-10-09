package pl.hellopoland.service.api.market;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.ArrayList;
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
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.config.TagPagedCollectionConfig;
import pl.hellopoland.dto.FilterDTO;
import pl.hellopoland.dto.FilterPriceEntryDTO;
import pl.hellopoland.dto.SearchResultDTO;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.CategoryService;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TagService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

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

    List<SightEvent> ses = getSightEvents(languageVersion, query, categoryIds, tagIds, city,
        fromDate, toDate, minPrice, maxPrice);

    Map<Sight, List<SightEvent>> ss =
        ses.stream().collect(groupingBy(SightEvent::getSight));

    SearchResultDTO oro = new SearchResultDTO();
    oro.sights = ss.entrySet().stream().map(entry -> {
      Sight s = tService.translateEntity(entry.getKey(), languageVersion, false);
      List<SightEvent> se = entry.getValue();
      List<Category> categories = se.stream()
          .filter(event -> event.getCategories() != null)
          .flatMap(event -> event.getCategories().stream())
          .map(SightEventCategory::getCategory)
          .distinct()
          .collect(toList());
      List<Tag> tags = se.stream()
          .filter(event -> event.getTags() != null)
          .flatMap(event -> event.getTags().stream())
          .map(SightEventTag::getTag)
          .distinct()
          .collect(toList());

      SightDTO dto = DtoMapper.getDTO(s);
      dto.sightEvents = se.stream()
          .map(DtoMapper::getDTO)
          .collect(toList());
      dto.minPrice = dto.sightEvents.stream()
          .map(sedto -> sedto.minPrice)
          .filter(Objects::nonNull)
          .min(Comparator.naturalOrder())
          .orElse(null);
      dto.categories = tService.translateEntities(categories, languageVersion, false).stream()
          .map(DtoMapper::getDTO)
          .collect(toSet());
      dto.tags = tService.translateEntities(tags, languageVersion, false).stream()
          .map(DtoMapper::getDTO)
          .collect(toSet());
      return dto;
    }).collect(toList());
    return oro;
  }

  private List<SightEvent> getSightEvents(LanguageVersion languageVersion,
      String query, Long[] categoryIds, Long[] tagIds, String city, Date fromDate, Date toDate,
      Integer minPrice,
      Integer maxPrice) {
    SightEventPagedCollectionConfig seConfig = new SightEventPagedCollectionConfig();
    if (fromDate != null && toDate != null && toDate.before(fromDate)) {
      throw new ConflictingException("toDate[" + toDate + "] is before fromDate[" + fromDate + "]");
    }
    if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
      throw new ConflictingException(
          "minPrice[" + minPrice + "] is lesser then maxPrice[" + maxPrice + "]");
    }
    seConfig.onlyAvailable();
    seConfig.onlyActive();
    seConfig.onlyPublished();
    seConfig.setOrderColumn("random()");
    seConfig.setCategoriesIdsArray(categoryIds);
    seConfig.setTagsIdsArray(tagIds);
    seConfig.setSearchQuery(query);
    seConfig.setCity(city);
    seConfig.setFetchCategories(true);
    seConfig.setFetchTags(true);
    PagedEntityCollection<SightEvent> sesPagedList = seService.getList(seConfig, languageVersion);
    List<SightEvent> ses =
        sesPagedList.items.stream().filter(SightEvent::isAccessible).collect(toList());
    if (!ses.isEmpty()) {
      if (fromDate == null) {
        fromDate = new Date();
      }
      HelloTicket hptClient = new HelloTicket(seService.getPortal("Hello Ticket Cloud").getUrl());
      ses = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(ses),
          fromDate, toDate);
      if (minPrice != null) {
        ses = ses.stream().filter(se -> se.getMinPrice() >= minPrice)
            .collect(toList());
      }
      if (maxPrice != null) {
        ses = ses.stream().filter(se -> se.getMinPrice() <= maxPrice)
            .collect(toList());
      }
    }
    return ses;
  }

  @PermitAll
  public FilterDTO filters() {
    FilterDTO filter = new FilterDTO();
    filter.prices = predefinedPriceFilters;
    filter.city = seService.getCitiesForPublicEvents();
    filter.categories = catService.pagedList(new CategoryPagedCollectionConfig()).items.stream()
        .map(DtoMapper::getDTO).collect(toList());
    filter.tags = tagService.pagedList(new TagPagedCollectionConfig()).items.stream()
        .map(DtoMapper::getDTO).collect(toList());
    return filter;
  }

}
