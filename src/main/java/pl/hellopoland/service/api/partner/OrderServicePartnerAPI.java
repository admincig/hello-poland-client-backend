package pl.hellopoland.service.api.partner;

import javax.annotation.security.RolesAllowed;
import pl.hellopoland.service.OrderService;

public class OrderServicePartnerAPI {
  private final System.Logger logger = System.getLogger(this.getClass().getName());

  private OrderService service;

  @RolesAllowed("partner")
  public boolean printTicketCopy(String P24Statement) {
    service.printTicketCopy(P24Statement);
  }

}
