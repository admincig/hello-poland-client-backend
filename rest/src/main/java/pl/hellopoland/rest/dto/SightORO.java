package pl.hellopoland.rest.dto;

import pl.hellopoland.model.Sight;

public class SightORO {
  public Long id;
  public String name;
  public String mainImage;

  public SightORO(Sight f) {
    this.id = f.getId();
    this.name = f.getName();
    this.mainImage = "/images/" + f.getMainImage().getHash() + f.getMainImage().getExtension();
  }
}
