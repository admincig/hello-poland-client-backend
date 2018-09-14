package pl.hellopoland.config;

import pl.hellopoland.bo.Sight;

public class SightPagedCollectionConfig extends PagedCollectionConfig<Sight> {

  private boolean currentPartner;

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi";
  }

  public void setSearchQuery(String searchQuery) {
    if (searchQuery != null) {
      addCondition("searchQuery", "%" + searchQuery.toLowerCase() + "%",
          "lower(e.name) like :searchQuery");
    }
  }

  public void setName(String name) {
    if (name != null) {
      addCondition("name", name.toLowerCase(), "lower(e.name)=:name");
    }
  }

  public void setDescription(String description) {
    addCondition("description", description, "e.description=:description");
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
    addCondition("partner", partnerId, "e.partner.id=:partner");
  }

  public void setCity(String city) {
    if (city != null) {
      addCondition("city", city.toLowerCase(), "lower(e.location.city)=:city");
    }
  }

}
