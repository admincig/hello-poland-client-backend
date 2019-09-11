package pl.hellopoland.service.api.helpdesk;

import java.io.File;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.AnalyticsService;
import pl.hellopoland.service.HellopolandService;
import pl.hellopoland.service.OrderService;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class ServiceHelpdeskAPI {
  @Inject
  private HellopolandService service;
  @Inject
  private AnalyticsService analyticsService;
  @Inject
  private SightEventService seService;
  @Inject
  private OrderService orderService;
  @Inject
  private TranslationService tService;

  @RolesAllowed({"admin", "salesman"})
  public PartnerDTO addPartner(PartnerDTO partner) {
    return DtoMapper.getFullDTO(service.addPartner(partner));
  }

  @RolesAllowed("admin")
  public PagedCollection listPartners(PartnerPagedCollectionConfig config) {
    PagedEntityCollection<Partner> bos = service.getList(config);
    var dtos = bos.items.stream().map(DtoMapper::getFullDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return analyticsService.getOrdersCsvFile(fromDate, toDate);
  }

  @RolesAllowed("admin")
  public PagedCollection getSightEvents(SightEventPagedCollectionConfig config,
      LanguageVersion language) {
    config.onlyActive();
    PagedEntityCollection<SightEvent> bos = seService.getList(config, language);
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

  @RolesAllowed("admin")
  public EmailSendingReportDTO sendTicketCopy(String P24Statement) {
    return orderService.sendTicketCopy(P24Statement);
  }

  @RolesAllowed("admin")
  public void deleteSightEvent(Long id) {
    seService.delete(id);
  }

  @RolesAllowed("admin")
  public SightEventDTO updateSightEvent(SightEventDTO dto, LanguageVersion language) {
    SightEvent bo = seService.get(dto.id);
    bo = seService.update(bo, dto, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public void deleteSightEventLanguageVersion(Long id, LanguageVersion language) {
    SightEvent bo = seService.get(id);
    tService.deleteEntityTranslations(bo, language);
  }

}
