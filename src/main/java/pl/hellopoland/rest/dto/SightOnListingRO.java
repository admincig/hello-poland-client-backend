package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.SightEvent;

public class SightOnListingRO extends SightSimpleRO {

  public String description;
  public Integer minPrice;
  public String mainImage;

  public SightOnListingRO(SightEvent f) {
    super(f);
    this.description = f.getDescription();
    this.minPrice = f.getMinPrice();
    if (f.getMainImage() != null) {
      this.mainImage = "/images/" + f.getMainImage().getHash() + f.getMainImage().getExtension();
    }
  }
}
