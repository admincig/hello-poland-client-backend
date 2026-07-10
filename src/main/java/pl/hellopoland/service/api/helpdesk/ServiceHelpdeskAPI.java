package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.service.*;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.io.File;
import java.util.Date;

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

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public File getOrdersCsvFile(Date fromDate, Date toDate) {
    return analyticsService.getOrdersCsvFile(fromDate, toDate);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public EmailSendingReportDTO sendTicketCopy(String hash) {
    return orderService.sendTicketCopy(hash);
  }

  @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
      "helpdesk_content_manager", "helpdesk_support"})
  public EmailSendingReportDTO sendTicketCopyToEmail(String hash, String email) {
    return orderService.sendTicketCopyToEmail(hash, email);
  }

  @RolesAllowed({"root", "admin"})
  public void rebuildSearchIndices() {
    sightService.rebuildSearchIndices();
    sightEventService.rebuildSearchIndices();
  }

  @RolesAllowed({"root", "admin"})
  public void sudoAckOrder(String hash) {
    orderService.sudoAck(hash);
  }

  // TODO delete this
  @RolesAllowed({"root", "admin"})
  public void globalRework() {
    userService.globalRework();
  }
}
