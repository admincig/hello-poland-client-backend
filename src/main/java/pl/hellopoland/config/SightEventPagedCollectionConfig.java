package pl.hellopoland.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;

public class SightEventPagedCollectionConfig extends PagedCollectionConfig<SightEvent> {

  private boolean currentPartner;
  private boolean fetchCategories;
  private boolean fetchTags;

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi join fetch e.partner p join fetch e.sight s";
  }

  public void setSearchQuery(String searchQuery) {
    if (searchQuery != null) {
      addCondition("searchQuery",
          searchQuery, "tsearch('polish_hunspell', e.searchIndex, :searchQuery) = true");
      /*
        "%" + searchQuery.toLowerCase() + "%",
        "((unaccent(lower(e.name)) like unaccent(:searchQuery))" +
        " or (unaccent(lower(e.lead)) like unaccent(:searchQuery))" +
        " or (unaccent(lower(e.location.city)) like unaccent(:searchQuery)))");
       */
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
    addCondition("pBlocked", false, "e.partner.blocked=:pBlocked");
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
    if (categoriesIds != null && !categoriesIds.isEmpty()) {
      addCondition("ids", categoriesIds,
          "e.id in (select sightEvent.id from SightEventCategory where category.id in (:ids))");
    }
  }

  public void setTagsIds(List<Long> tagsIds) {
    if (tagsIds != null && !tagsIds.isEmpty()) {
      addCondition("ids", tagsIds,
          "e.id in (select sightEvent.id from SightEventTag where tag.id in (:ids))");
    }
  }

  public void setCategoriesIdsArray(Long[] categoryIds) {
    if (categoryIds != null && categoryIds.length > 0) {
      setCategoriesIds(Arrays.asList(categoryIds));
    }
  }

  public void setTagsIdsArray(Long[] tagIds) {
    if (tagIds != null && tagIds.length > 0) {
      setTagsIds(Arrays.asList(tagIds));
    }
  }

  public void setFetchCategories(boolean fetchCategories) {
    this.fetchCategories = fetchCategories;
  }

  public boolean isFetchCategories() {
    return this.fetchCategories;
  }

  public void setSight(Sight sight) {
    addCondition("sight", sight, "e.sight=:sight");
  }

  public void setExcludedIds(Set<Long> ids) {
    addCondition("ids", ids, "e.id not in (:ids)");
  }

  public void setFetchTags(boolean fetchTags) {
    this.fetchTags = fetchTags;
  }

  public boolean isFetchTags() {
    return fetchTags;
  }

}
