package pl.hellopoland.config;

import java.util.Arrays;
import java.util.List;
import pl.hellopoland.bo.SightEvent;

public class SightEventPagedCollectionConfig extends PagedCollectionConfig<SightEvent> {

  private boolean currentPartner;

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi join fetch e.partner p join fetch e.sight s";
  }

  public void setSearchQuery(String searchQuery) {
    if (searchQuery != null) {
      addCondition("searchQuery", "%" + searchQuery.toLowerCase() + "%",
          "((unaccent(lower(e.name)) like unaccent(:searchQuery))"
              + " or (unaccent(lower(e.lead)) like unaccent(:searchQuery))"
              + " or (unaccent(lower(e.location.city)) like unaccent(:searchQuery)))");
    }
  }

  public void setName(String name) {
    if (name != null) {
      addCondition("name", name.toLowerCase(), "lower(e.name)=:name");
    }
  }

  public void onlyAvailable() {
    addCondition("available", true, "e.available=:available");
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
    addCondition("partner", partnerId, "e.sight.partner.id=:partner");
  }

  public void setCity(String city) {
    if (city != null) {
      addCondition("city", city.toLowerCase(), "lower(e.location.city)=:city");
    }
  }

  public void setPromotion(Integer... promotions) {
    if (promotions.length > 0) {
      addCondition("promotions", Arrays.asList(promotions), "e.promotion in (:promotions)");
    }
  }

  public void setCategoriesIds(List<Long> categoriesIds) {
    addCondition("ids", categoriesIds, "e.categories.id in (:ids)");
  }

}
