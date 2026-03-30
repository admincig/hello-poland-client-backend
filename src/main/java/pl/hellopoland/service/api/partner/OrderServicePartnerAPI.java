package pl.hellopoland.service.api.partner;

import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.service.OrderService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class OrderServicePartnerAPI {

  @Inject
  private OrderService service;

  @RolesAllowed("partner")
  public EmailSendingReportDTO sendTicketCopy(String hash) {
    return service.sendTicketCopy(hash);
  }

    @RolesAllowed("partner")
    public void sendTicketCopyBySerialNumber(String serialNumber) {
        service.sendTicketCopyBySerialNumber(serialNumber);
    }
}
