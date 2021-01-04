package pl.hellopoland.rest.dto;

import java.util.Date;
import java.util.List;
import pl.hellopoland.bo.OrderDetails;

public class OrderIRO {

  public List<OrderEntryIRO> entries;
  public OrderDetails details;

  public static class OrderEntryIRO {
    public Long id;
    public Date date;
    public Integer quantity;
    public String partnerAffiliateCode;
    public Integer price;
  }
}
