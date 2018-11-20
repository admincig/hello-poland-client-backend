package pl.hellopoland.service.api.market;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightServiceMarketAPI {

  @Inject
  SightService service;

  @Inject
  private TranslationService translationService;

  @PermitAll
  public PagedCollection getList(SightPagedCollectionConfig config, String language) {
    config.setOrderColumn("name");
    config.setOrderDirection("asc");
    PagedEntityCollection<Sight> bos = service.getList(config);
    if (language != null && !language.toLowerCase().contains("pl")) {
      bos.items = translationService.translateEntities(bos.items, language, false);
    }
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public SightDTO get(Long id, String language) {
    Sight bo = service.get(id);
    if (bo.isPublished()) {
      bo.setSightEvents(bo.getSightEvents().stream()
          .filter(se -> se.isActive() && se.isPublished() && !se.isBlocked())
          .collect(Collectors.toList()));
      if (language != null && !language.toLowerCase().contains("pl")) {
        bo = translationService.translateEntity(bo, language, true);
        var agreements = bo.getAgreements();
        var sightEvents = bo.getSightEvents();
        if (agreements != null && !agreements.isEmpty()) {
          bo.setAgreements(
              Set.copyOf(translationService.translateEntities(agreements, language, true)));
        }
        if (sightEvents != null && !sightEvents.isEmpty()) {
          bo.setSightEvents(translationService.translateEntities(sightEvents, language, true));
        }
      }
      var dto = DtoMapper.getFullDTO(bo);
      return dto;
    }
    return null;
  }

}
