package pl.hellopoland.rest.dto;

import pl.hellopoland.model.Facility;

public class FacilityORO {
  public Long id;
  public String name;
  public String mainImage;

  public FacilityORO(Facility f) {
    this.id = f.getId();
    this.name = f.getName();
    this.mainImage = "/images/" + f.getMainImage().getHash() + f.getMainImage().getExtension();
  }
}
