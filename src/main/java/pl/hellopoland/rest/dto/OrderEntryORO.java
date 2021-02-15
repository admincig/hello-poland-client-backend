package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.OrderEntry;

import java.util.List;

public class OrderEntryORO extends OrderEntryOnListingORO {

  public List<String> numbers;

  public OrderEntryORO(OrderEntry oe) {
    super(oe);
    this.numbers = oe.getNumbers();
  }

}
