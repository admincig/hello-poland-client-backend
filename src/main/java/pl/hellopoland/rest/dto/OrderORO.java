package pl.hellopoland.rest.dto;

import java.util.List;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.P24PassageCartEntry;

public class OrderORO {

  public OrderORO(Order o) {
    this.hash = o.getHash();
    this.p24PassageCartEntries = o.getP24PassageCartEntries();
  }

  public String hash;
  public List<P24PassageCartEntry> p24PassageCartEntries;
}
