package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.service.OrderService;

@Stateless
public class OrderServicePartnerAPI {
  private final System.Logger logger = System.getLogger(this.getClass().getName());

  @Inject
  private OrderService service;

  @RolesAllowed("partner")
  public EmailSendingReportDTO sendTicketCopy(String P24Statement) {
    return service.sendTicketCopy(P24Statement);
  }

}
