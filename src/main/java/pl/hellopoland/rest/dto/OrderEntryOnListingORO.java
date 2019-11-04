package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.OrderEntry;

public class OrderEntryOnListingORO {

  public String name;
  public Integer quantity;
  public Integer unitPrice;

  public OrderEntryOnListingORO(OrderEntry oe) {
    this.quantity = oe.getQuantity();
    this.name = oe.getName();
    this.unitPrice = oe.getUnitPrice();
  }
}
