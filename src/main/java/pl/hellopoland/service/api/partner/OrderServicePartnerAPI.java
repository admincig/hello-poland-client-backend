package pl.hellopoland.service.api.partner;

import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.service.OrderService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class OrderServicePartnerAPI {

  @Inject
  private OrderService service;

  @RolesAllowed("partner")
  public EmailSendingReportDTO sendTicketCopy(String hash) {
    return service.sendTicketCopy(hash);
  }

}
