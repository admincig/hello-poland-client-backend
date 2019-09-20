package pl.hellopoland.service.api.market;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.CategoryPagedCollectionConfig;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.FilterDTO;
import pl.hellopoland.dto.FilterPriceEntryDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.SearchResultORO;
import pl.hellopoland.rest.dto.SightRO;
import pl.hellopoland.service.CategoryService;
import pl.hellopoland.service.SightEventService;
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
  public SearchResultORO search(LanguageVersion languageVersion, String query, Long[] categoryIds,
      String city, Date fromDate, Date toDate, Integer minPrice, Integer maxPrice) {

    SightEventPagedCollectionConfig seConfig = new SightEventPagedCollectionConfig();
    if (fromDate != null && toDate != null && toDate.before(fromDate)) {
      throw new ConflictingException("toDate[" + toDate + "] is before fromDate[" + fromDate + "]");
    }
    seConfig.onlyAvailable();
    seConfig.onlyActive();
    seConfig.onlyPublished();
    seConfig.setOrderColumn("random()");
    seConfig.setCategoriesIds(categoryIds);
    seConfig.setSearchQuery(query);
    seConfig.setCity(city);
    PagedEntityCollection<SightEvent> ses = seService.getList(seConfig, languageVersion);
    ses.items = ses.items.stream().filter(SightEvent::isAccessible).collect(Collectors.toList());
    if (fromDate != null || toDate != null) {
      HelloTicket hptClient = new HelloTicket(seService.getPortal("Hello Ticket Cloud").getUrl());
      ses.items = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(ses.items),
          fromDate, toDate);
      if (minPrice != null) {
        ses.items = ses.items.stream().filter(se -> se.getMinPrice() >= minPrice)
            .collect(Collectors.toList());
      }
      if (maxPrice != null) {
        ses.items = ses.items.stream().filter(se -> se.getMinPrice() <= maxPrice)
            .collect(Collectors.toList());
      }
    }
    Map<Sight, List<SightEvent>> ss =
        ses.items.stream().collect(Collectors.groupingBy(SightEvent::getSight));

    SearchResultORO oro = new SearchResultORO();
    oro.sights = ss.entrySet().stream().map(entry -> {
      Sight s = tService.translateEntity(entry.getKey(), languageVersion, true);
      List<SightEvent> se = entry.getValue();
      s.setSightEvents(tService.translateEntities(se, languageVersion, false));
      return new SightRO(s);
    }).collect(Collectors.toList());
    return oro;
  }

  @PermitAll
  public FilterDTO filters() {
    FilterDTO filter = new FilterDTO();
    filter.prices = predefinedPriceFilters;
    filter.city = seService.getCitiesForPublicEvents();
    filter.categories = catService.pagedList(new CategoryPagedCollectionConfig()).items.stream()
        .map(DtoMapper::getDTO).collect(Collectors.toList());
    return filter;
  }

}
