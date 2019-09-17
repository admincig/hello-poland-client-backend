package pl.hellopoland.service.api.market;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.SearchResultORO;
import pl.hellopoland.rest.dto.SightEventSimpleRO;
import pl.hellopoland.rest.dto.SightRO;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.SightService;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SearchServiceMarketAPI {

  @Inject
  SightEventService seService;
  @Inject
  SightService sService;

  @PermitAll
  public SearchResultORO search(
      List<Long> categoriesIds,
      LanguageVersion languageVersion) {
    SightEventPagedCollectionConfig seConfig = new SightEventPagedCollectionConfig();
    seConfig.setCategoriesIds(categoriesIds);
    PagedEntityCollection<SightEvent> ses = seService.getList(seConfig, languageVersion);

    SightPagedCollectionConfig sConfig = new SightPagedCollectionConfig();
    PagedEntityCollection<Sight> ss = sService.getList(sConfig, languageVersion);

    SearchResultORO oro = new SearchResultORO();
    oro.sightEvents = ses.items.stream().map(SightEventSimpleRO::new).collect(Collectors.toList());
    oro.sights = ss.items.stream().map(SightRO::new).collect(Collectors.toList());
    return oro;
  }

}
