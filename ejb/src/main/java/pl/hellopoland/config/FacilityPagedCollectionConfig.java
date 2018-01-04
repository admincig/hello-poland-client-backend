package pl.hellopoland.config;

import pl.hellopoland.model.Facility;

public class FacilityPagedCollectionConfig extends PagedCollectionConfig<Facility> {

  @Override
  public String joins() {
    return "join fetch e.mainImage mi";
  }

  public void setSearchQuery(String searchQuery) {
    addCondition(new Entry("searchQuery", "%" + searchQuery.toLowerCase() + "%",
        "lower(e.name) like :searchQuery"));
  }

  public void setName(String name) {
    addCondition(new Entry("name", name, "e.name=:name"));
  }
}
