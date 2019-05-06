package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.service.OrderService;

public class OrderServicePartnerAPI {
  private final System.Logger logger = System.getLogger(this.getClass().getName());

  private OrderService service;

  @RolesAllowed("partner")
  public EmailSendingReportDTO sendTicketCopy(String P24Statement) {
    return service.sendTicketCopy(P24Statement);
  }

}
