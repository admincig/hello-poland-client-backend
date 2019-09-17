package pl.hellopoland.rest.dto;

import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;

public class SightRO {

  public Long id;
  public String name;
  public String mainImage;
  public List<Long> sightEventIds;

  public SightRO(Sight sight) {
    this.id = sight.getId();
    this.name = sight.getName();
    this.mainImage = sight.getMainImage().getImageURL();
    this.sightEventIds =
        sight.getSightEvents().stream().map(SightEvent::getId).collect(Collectors.toList());
  }
}
