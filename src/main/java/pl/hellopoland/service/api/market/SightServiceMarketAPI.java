package pl.hellopoland.service.api.market;

import java.util.Comparator;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.SightService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
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
    config.setOrderColumn("name");
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
          .filter(se -> se.isActive() && se.isPublished() && !se.isBlocked())
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
      return dto;
    }
    return null;
  }

}
