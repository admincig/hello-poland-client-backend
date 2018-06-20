package pl.hellopoland.config;

import pl.hellopoland.sight.SightEvent;

public class SightsPagedCollectionConfig extends PagedCollectionConfig<SightEvent> {

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
}
