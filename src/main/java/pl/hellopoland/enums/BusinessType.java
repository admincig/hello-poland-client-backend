package pl.hellopoland.enums;

import java.util.Arrays;

public enum BusinessType {
//@formatter:off
  NATURAL_PERSON(1), SELF_EMPLOYED_ACTIVITY(2), PARTNERSHIP(3), GENERAL_PARTNERSHIP(4),
  LIMITED_PARTNERSHIP(5), LIMITED_PARTNERSHIP_BY_SHARES(6), JOIN_STOCK_COMPANY(7),
  LIMITED_LIABILITY_COMPANY(8), FUNDATION(9), COOPERATIVE(10);
//@formatter:on
  private int value;

  private BusinessType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static BusinessType getBusinessType(int value) {
    return Arrays.stream(BusinessType.values()).filter(bt -> bt.getValue() == value).findFirst()
        .orElse(null);
  }

}
