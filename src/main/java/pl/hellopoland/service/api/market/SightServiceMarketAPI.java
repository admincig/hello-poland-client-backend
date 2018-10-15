package pl.hellopoland.service.api.market;

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
    PagedEntityCollection<Sight> bos = service.getList(config);


    if (language != null && !language.toLowerCase().contains("pl")) {
      bos.items = translationService.translateEntities(bos.items, language);
    }



    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public SightDTO get(Long id, String language) {
    Sight bo = service.get(id);
    bo.setSightEvents(
        bo.getSightEvents().stream().filter(se -> se.isActive()).collect(Collectors.toList()));
    if (language != null && !language.toLowerCase().contains("pl")) {
      fetchColections(bo);
      bo = translationService.translateEntity(bo, language);
    }
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  private void fetchColections(Sight bo) {
    if (bo.getSightEvents() != null && !bo.getSightEvents().isEmpty()) {
      bo.getSightEvents().size();
    }
    if (bo.getImages() != null && !bo.getImages().isEmpty()) {
      bo.getImages().size();
    }
    if (bo.getOpeningHours() != null && !bo.getOpeningHours().isEmpty()) {
      bo.getOpeningHours().size();
    }
    if (bo.getAgreements() != null && !bo.getAgreements().isEmpty()) {
      bo.getAgreements().size();
    }
  }

}
