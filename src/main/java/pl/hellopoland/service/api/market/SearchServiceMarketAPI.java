package pl.hellopoland.service.api.market;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.SearchResultORO;
import pl.hellopoland.rest.dto.SightEventSimpleRO;
import pl.hellopoland.rest.dto.SightRO;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.SightService;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SearchServiceMarketAPI {

  @Inject
  SightEventService seService;
  @Inject
  SightService sService;

  @PermitAll
  public SearchResultORO search(LanguageVersion languageVersion, String query, Long[] categoryIds,
      String city, Date fromDate, Date toDate) {
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
    ses.items = ses.items.stream().filter(se -> se.isAccessible()).collect(Collectors.toList());
    if (fromDate != null || toDate != null) {
      HelloTicket hptClient = new HelloTicket(seService.getPortal("Hello Ticket Cloud").getUrl());
      ses.items = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(ses.items),
          fromDate, toDate);
    }

    SightPagedCollectionConfig sConfig = new SightPagedCollectionConfig();
    sConfig.setOrderColumn("random()");
    sConfig.setCity(city);
    sConfig.setSearchQuery(query);
    PagedEntityCollection<Sight> ss = sService.getList(sConfig, languageVersion);

    SearchResultORO oro = new SearchResultORO();
    oro.sightEvents = ses.items.stream().map(SightEventSimpleRO::new).collect(Collectors.toList());
    oro.sights = ss.items.stream().map(SightRO::new).collect(Collectors.toList());
    return oro;
  }

}
