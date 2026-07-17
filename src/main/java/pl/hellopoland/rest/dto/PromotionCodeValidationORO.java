package pl.hellopoland.rest.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.bind.annotation.JsonbTransient;
import pl.hellopoland.enums.PromotionScopeType;
import pl.hellopoland.enums.PromotionType;

public class PromotionCodeValidationORO {

  public enum Status {
    VALID,
    INVALID
  }

  public Status status;
  public String errorCode;
  public String message;
  public String reservationToken;
  public Promotion promotion;
  public List<Effect> effects = new ArrayList<>();

  public static class Promotion {
    public Long id;
    public String name;
    public PromotionType type;
    public PromotionScopeType scope;
  }

  public static class Effect {
    public String type;
    public Long sourceCartItemId;
    public SightEventRef sightEvent;
    public Item item;
    public BigDecimal discountPercent;
    public Integer discountAmountGross;
    public Target target;
  }

  public static class SightEventRef {
    public Long id;
  }

  public static class Item {
    public Long id;
    public String name;
    public Integer quantity;
    public Integer price;
    public Integer originalPrice;
    public TicketTypeRef ticketType;
    public PoolRef pool;
  }

  public static class TicketTypeRef {
    public Long id;
    public String code;
    public String label;
  }

  public static class PoolRef {
    public Long id;
    public String type;
    public String name;
  }

  public static class Target {
    public String type;
    public List<Long> cartItemIds;
    public List<Long> sightEventIds;
  }

  public static PromotionCodeValidationORO valid() {
    PromotionCodeValidationORO oro = new PromotionCodeValidationORO();
    oro.status = Status.VALID;
    return oro;
  }

  public static PromotionCodeValidationORO invalid(String errorCode, String message) {
    PromotionCodeValidationORO oro = new PromotionCodeValidationORO();
    oro.status = Status.INVALID;
    oro.errorCode = errorCode;
    oro.message = message;
    return oro;
  }

  @JsonbTransient
  public boolean isInvalid() {
    return status != Status.VALID;
  }
}
