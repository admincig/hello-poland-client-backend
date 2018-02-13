package pl.hellopoland.rest.dto;

import java.util.List;
import pl.hellopoland.order.OrderEntry;

public class OrderEntryORO extends OrderEntryOnListingORO {

  public List<String> numbers;

  public OrderEntryORO(OrderEntry oe) {
    super(oe);
    this.numbers = oe.getNumbers();
  }

}
