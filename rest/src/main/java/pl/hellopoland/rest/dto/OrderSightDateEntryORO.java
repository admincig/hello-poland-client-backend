package pl.hellopoland.rest.dto;

import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.order.OrderDetails;
import pl.hellopoland.order.OrderSightDateEntry;

public class OrderSightDateEntryORO extends OrderSightDateEntryOnListingORO {
  public String buyer;
  public List<OrderEntryORO> entries;

  public OrderSightDateEntryORO(OrderSightDateEntry osde) {
    super(osde);
    this.entries = osde.getEntries().stream().map(OrderEntryORO::new).collect(Collectors.toList());
    OrderDetails details = osde.getSightEntry().getOrder().getDetails();
    this.buyer = details.getFirstName() + ' ' + details.getLastName();
  }

}
