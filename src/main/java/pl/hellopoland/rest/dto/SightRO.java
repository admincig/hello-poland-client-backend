package pl.hellopoland.rest.dto;

import java.util.List;
import java.util.stream.Collectors;
import pl.hellopoland.bo.Sight;

public class SightRO {

  public Long id;
  public String name;
  public String mainImage;
  public List<SightEventSimpleRO> sightEvents;

  public SightRO(Sight sight) {
    this.id = sight.getId();
    this.name = sight.getName();
    if (sight.getMainImage() != null) {
      this.mainImage = sight.getMainImage().getImageURL();
    }
    this.sightEvents =
        sight.getSightEvents().stream().map(SightEventSimpleRO::new).collect(Collectors.toList());
  }
}
