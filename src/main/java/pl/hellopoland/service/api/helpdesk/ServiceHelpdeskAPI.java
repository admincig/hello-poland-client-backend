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
import pl.hellopoland.service.UserService;

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
  @Inject
  private UserService userService;

  @RolesAllowed("admin")
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return analyticsService.getOrdersCsvFile(fromDate, toDate);
  }

  @RolesAllowed("admin")
  public EmailSendingReportDTO sendTicketCopy(String hash) {
    return orderService.sendTicketCopy(hash);
  }

  @RolesAllowed("admin")
  public void rebuildSearchIndices() {
    sightService.rebuildSearchIndices();
    sightEventService.rebuildSearchIndices();
  }

  @RolesAllowed("admin")
  public void sudoAckOrder(String hash) {
    orderService.sudoAck(hash);
  }

  // TODO delete this
  @RolesAllowed("admin")
  public void globalRework() {
    userService.globalRework();
  }
}
