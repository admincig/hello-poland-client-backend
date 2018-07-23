package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.OpeningHours;

public class OpeningHoursRO {

  public Integer day;
  public String openTime;
  public String closeTime;

  public OpeningHoursRO(OpeningHours oh) {
    this.day = oh.getDay();
    this.openTime = oh.getOpenTime().toString();
    this.closeTime = oh.getCloseTime().toString();
  }
}
