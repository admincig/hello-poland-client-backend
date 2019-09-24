package pl.hellopoland.config;

import java.util.Set;
import pl.hellopoland.bo.Sight;

public class SightPagedCollectionConfig extends PagedCollectionConfig<Sight> {

  private boolean currentPartner;

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi";// join fetch e.sightEvents ses";
  }

  public void setSearchQuery(String searchQuery) {
    if (searchQuery != null) {
      addCondition("searchQuery",
          searchQuery, "tsearch('polish_hunspell', e.searchIndex, :searchQuery) = true");
      /*
        "%" + searchQuery.toLowerCase() + "%",
        "((unaccent(lower(e.name)) like unaccent(:searchQuery))"
            + " or (unaccent(lower(e.lead)) like unaccent(:searchQuery))"
            + " or (unaccent(lower(e.location.city)) like unaccent(:searchQuery)))");
      */
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

  public void onlyPublished() {
    addCondition("published", true, "e.published=:published");
    addCondition("blocked", false, "e.blocked=:blocked");
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

  public void setExcludedIds(Set<Long> ids) {
    addCondition("ids", ids, "e.id not in (:ids)");
  }

}
