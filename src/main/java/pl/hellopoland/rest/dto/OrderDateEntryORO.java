package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.bo.OrderDetails;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDateEntryORO extends OrderDateEntryOnListingORO {

  public String buyer;
  public List<OrderEntryORO> entries;

  public OrderDateEntryORO(OrderDateEntry ode) {
    super(ode);
    this.entries = ode.getEntries().stream().map(OrderEntryORO::new).collect(Collectors.toList());
    OrderDetails details = ode.getSightEntry().getOrder().getDetails();
    this.buyer = details.getFirstName() + ' ' + details.getLastName();
  }

}
