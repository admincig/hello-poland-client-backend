package pl.hellopoland.rest.dto;

import java.util.Collection;
import java.util.Date;
import pl.hellopoland.bo.OrderDetails;

public class OrderIRO {

  public Collection<OrderEntryIRO> entries;
  public OrderDetails details;

  public static class OrderEntryIRO {

    public Long id;
    public Date date;
    public Integer quantity;
  }
}
