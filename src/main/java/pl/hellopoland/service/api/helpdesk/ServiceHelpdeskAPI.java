package pl.hellopoland.service.api.helpdesk;

import java.io.File;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.service.AnalyticsService;
import pl.hellopoland.service.OrderService;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.SightService;

@Stateless
public class ServiceHelpdeskAPI {
  @Inject
  private AnalyticsService analyticsService;
  @Inject
  private OrderService orderService;
  @Inject
  private SightService sightService;
  @Inject
  private SightEventService sightEventService;

  @RolesAllowed("admin")
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return analyticsService.getOrdersCsvFile(fromDate, toDate);
  }

  @RolesAllowed("admin")
  public EmailSendingReportDTO sendTicketCopy(String P24Statement) {
    return orderService.sendTicketCopy(P24Statement);
  }

  @RolesAllowed("admin")
  public void rebuildSearchIndices() {
    sightEventService.rebuildSearchIndices();
    sightService.rebuildSearchIndices();
  }

}
