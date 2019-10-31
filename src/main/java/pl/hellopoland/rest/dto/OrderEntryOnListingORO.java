package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.OrderEntry;

public class OrderEntryOnListingORO {

  public String name;
  public Integer quantity;

  // TODO
  // plus cena jednostkowa, pojedynczego biletu
  // nr zamówienia
  public OrderEntryOnListingORO(OrderEntry oe) {
    this.quantity = oe.getQuantity();
    this.name = oe.getName();
  }
}
