package pl.hellopoland.rest.dto;

import pl.hellopoland.model.Sight;

public class SightOnListingRO {
  public Long id;
  public String name;
  public Integer minPrice;
  public String mainImage;

  public SightOnListingRO(Sight f) {
    this.id = f.getId();
    this.name = f.getName();
    this.minPrice = f.getMinPrice();
    this.mainImage = "/images/" + f.getMainImage().getHash() + f.getMainImage().getExtension();
  }
}
