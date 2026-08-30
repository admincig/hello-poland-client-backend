package pl.hellopoland.rest.dto;

import java.util.Date;
import java.util.List;

public class PromotionCodeValidationIRO {

  public String code;
  public List<CartItem> cartItems;

  public static class CartItem {
    public Long id;
    public Integer quantity;
    public Date date;
  }
}
