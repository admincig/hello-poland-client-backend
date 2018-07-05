package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.SightEvent;

public class SightEventOnListingRO extends SightEventSimpleRO {

  public String description;
  public Integer minPrice;
  public String mainImage;

  public SightEventOnListingRO(SightEvent f) {
    super(f);
    this.description = f.getDescription();
    this.minPrice = f.getMinPrice();
    if (f.getMainImage() != null) {
      this.mainImage = f.getMainImage().getDownloadUrl();
    }
  }
}
