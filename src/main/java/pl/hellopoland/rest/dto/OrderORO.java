package pl.hellopoland.rest.dto;

import pl.hellopoland.order.Order;

public class OrderORO {

  public OrderORO(Order o) {
    this.hash = o.getHash();
  }

  public String hash;
}
