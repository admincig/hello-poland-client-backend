package pl.hellopoland.service.api.helpdesk;

import java.io.File;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.AnalyticsService;
import pl.hellopoland.service.HellopolandService;
import pl.hellopoland.service.OrderService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class ServiceHelpdeskAPI {
  @Inject
  private HellopolandService service;
  @Inject
  private AnalyticsService analyticsService;
  @Inject
  private OrderService orderService;

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
  public EmailSendingReportDTO sendTicketCopy(String P24Statement) {
    return orderService.sendTicketCopy(P24Statement);
  }

}
