package pl.hellopoland.rest.dto;

import pl.hellopoland.bo.SightEvent;

public class SightEventSimpleRO {

  public Long id;
  public String name;
  public SightLocationRO location;
  public Long sightId;
  public Integer minPrice;

  public SightEventSimpleRO(SightEvent s) {
    this.id = s.getId();
    this.name = s.getName();
    if (s.getLocation() != null) {
      this.location = new SightLocationRO(s.getLocation());
    }
    if (s.getSight() != null) {
      this.sightId = s.getSight().getId();
    }
    this.minPrice = s.getMinPrice();
  }
}
