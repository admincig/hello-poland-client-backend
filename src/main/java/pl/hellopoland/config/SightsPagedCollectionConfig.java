package pl.hellopoland.config;

import pl.hellopoland.bo.SightEvent;

public class SightsPagedCollectionConfig extends PagedCollectionConfig<SightEvent> {

  private boolean currentPartner;

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi";
  }

  public void setSearchQuery(String searchQuery) {
    addCondition("searchQuery", "%" + searchQuery.toLowerCase() + "%",
        "lower(e.name) like :searchQuery");
  }

  public void setName(String name) {
    addCondition("name", name, "e.name=:name");
  }

  public void onlyActive() {
    addCondition("active", true, "e.active=:active");
  }

  public void onlyCurrentPartner(boolean only) {
    currentPartner = only;
  }

  public boolean isCurrentPartner() {
    return currentPartner;
  }

  public void setPartner(Long partnerId) {
    addCondition("partner", partnerId, "e.sight.partner.id=:partner");
  }
}
