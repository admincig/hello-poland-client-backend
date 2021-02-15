package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.OrderDetails;

import java.util.Date;
import java.util.List;

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
