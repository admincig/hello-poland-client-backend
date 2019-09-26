package pl.hellopoland.config;

import pl.hellopoland.bo.Partner;

public class PartnerPagedCollectionConfig extends PagedCollectionConfig<Partner> {

  public void setBlocked(boolean blocked) {
    addCondition("blocked", blocked, "e.blocked=:blocked");
  }
}
