package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.Sight;

public class SightOnListingRO extends SightSimpleRO {
  public String description;
  public Integer minPrice;
  public String mainImage;

  public SightOnListingRO(Sight f) {
    super(f);
    this.description = f.getDescription();
    this.minPrice = f.getMinPrice();
    this.mainImage = "/images/" + f.getMainImage().getHash() + f.getMainImage().getExtension();
  }
}
