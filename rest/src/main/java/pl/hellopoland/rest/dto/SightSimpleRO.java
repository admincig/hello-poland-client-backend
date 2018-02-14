package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.Sight;

public class SightSimpleRO {
  public Long id;
  public String name;
  public SightLocationRO location;

  public SightSimpleRO(Sight s) {
    this.id = s.getId();
    this.name = s.getName();
    this.location = new SightLocationRO(s.getLocation());
  }
}
