package pl.hellopoland.rest.dto;

import java.util.Map;
import pl.hellopoland.bo.Order;

public class OrderORO {

  public OrderORO(Order o) {
    this.hash = o.getHash();
    this.paymentsByP24PartnerId = o.getSumBillsByP24PartnerId();
  }

  public String hash;
  public Map<String, Integer> paymentsByP24PartnerId;
}
