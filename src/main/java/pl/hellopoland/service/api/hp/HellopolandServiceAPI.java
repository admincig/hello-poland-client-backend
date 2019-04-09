package pl.hellopoland.service.api.hp;

import java.io.File;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.AnalyticsService;
import pl.hellopoland.service.HellopolandService;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class HellopolandServiceAPI {
  @Inject
  private HellopolandService service;
  @Inject
  private AnalyticsService analyticsService;
  @Inject
  private SightEventService seService;

  @RolesAllowed("admin")
  public PartnerDTO addPartner(PartnerDTO partner) {
    return DtoMapper.getFullDTO(service.addPartner(partner));
  }

  @RolesAllowed("admin")
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return analyticsService.getOrdersCsvFile(fromDate, toDate);
  }

  @RolesAllowed("admin")
  public PagedCollection getSightEvents(SightEventPagedCollectionConfig config) {
    config.onlyActive();
    PagedEntityCollection<SightEvent> bos = seService.getList(config, LanguageVersion.PL_PL);
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public void setSightEventPromotion(Long id, Integer promotion) {
    seService.setSightEventPromotion(id, promotion);
  }

  @RolesAllowed("admin")
  public void removeSightEventPromotion(Long id) {
    seService.removeSightEventPromotion(id);
  }

}
