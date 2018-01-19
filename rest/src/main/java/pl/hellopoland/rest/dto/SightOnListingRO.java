package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.Sight;

public class SightOnListingRO {
  public Long id;
  public String name;
  public String description;
  public Integer minPrice;
  public String mainImage;

  public SightOnListingRO(Sight f) {
    this.id = f.getId();
    this.name = f.getName();
    this.description = f.getDescription();
    this.minPrice = f.getMinPrice();
    this.mainImage = "/images/" + f.getMainImage().getHash() + f.getMainImage().getExtension();
  }
}
