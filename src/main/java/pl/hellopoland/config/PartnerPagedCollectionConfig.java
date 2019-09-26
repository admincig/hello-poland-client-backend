package pl.hellopoland.config;

import pl.hellopoland.bo.Partner;

public class PartnerPagedCollectionConfig extends PagedCollectionConfig<Partner> {

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi";
  }

  public void setBlocked(boolean blocked) {
    addCondition("blocked", blocked, "e.blocked=:blocked");
  }
}
