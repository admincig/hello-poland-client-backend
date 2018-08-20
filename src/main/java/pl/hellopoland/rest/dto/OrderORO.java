package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.P24PassageOrder;
import pl.hellopoland.bo.P24PassageTransactionParams;

public class OrderORO {

  public OrderORO(P24PassageOrder p24Order) {
    this.isSandbox = p24Order.isSandbox();
    this.transactionParams = p24Order.getTransactionParams();
  }
  // public OrderORO(Order o) {
  // this.hash = o.getHash();
  // this.p24PassageCartEntries = o.getP24PassageCartEntries();
  // }

  public boolean isSandbox;
  public P24PassageTransactionParams transactionParams;
  // public String hash;
  // public List<P24PassageCartEntry> p24PassageCartEntries;
}
