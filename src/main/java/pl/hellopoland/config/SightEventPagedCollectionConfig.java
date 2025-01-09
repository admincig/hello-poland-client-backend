package pl.hellopoland.config;

import org.apache.commons.lang3.text.WordUtils;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.enums.LanguageVersion;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SightEventPagedCollectionConfig extends PagedCollectionConfig<SightEvent> {

  public SightEventPagedCollectionConfig() {
    this.setOrderColumn("e.name");
  }

  private boolean currentPartner;
  private boolean loggedUserFavourites;
  private boolean fetchCategories;
  private boolean fetchTags;
  private boolean fetchUsers;
  private LanguageVersion language;
  private Set<Long> excludedIds;
  private Date dateFrom;
  private Date dateTo;

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi "
        + "left join fetch mi.orginal mio "
        + "left join fetch mi.qvga miq "
        + "left join fetch mi.vga miv "
        + "left join fetch mi.hd mih "
        + "left join fetch mi.xga mix "
        + "left join fetch mi.sxga mis "
        + "left join fetch mi.fhd mif "
        + "left join fetch mi.fourK mi4 "
        + "left join fetch mi.orginalWebp miow "
        + "left join fetch mi.qvgaWebp miqw "
        + "left join fetch mi.vgaWebp mivw "
        + "left join fetch mi.hdWebp mihw "
        + "left join fetch mi.xgaWebp mixw "
        + "left join fetch mi.sxgaWebp mivsw "
        + "left join fetch mi.fhdWebp mifw "
        + "left join fetch mi.fourKWebp mi4w "
        + "left join fetch e.partner p "
        + "left join fetch p.contactPerson pc "
        + "left join fetch p.technicalContact tc "
        + "left join fetch e.portal port "
        + "join fetch e.sight s "
        + "left join fetch s.mainImage smi "
        + (fetchCategories ? " left join fetch e.categories cs" : "")
        + (fetchUsers ? " inner join e.users u" : "");
  }

  public void setSearchQuery(String searchQuery) {
      if (searchQuery != null) {
      addCondition("searchQuery",
          WordUtils.capitalizeFully(searchQuery),
          "tsearch('polish_hunspell', e.searchIndex, :searchQuery) = true");

      /*
       * "%" + searchQuery.toLowerCase() + "%",
       * "((unaccent(lower(e.name)) like unaccent(:searchQuery))" +
       * " or (unaccent(lower(e.lead)) like unaccent(:searchQuery))" +
       * " or (unaccent(lower(e.location.city)) like unaccent(:searchQuery)))");
       */
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

  public void onlyFavourite(Long userId) {
    fetchUsers = true;
    addCondition("userId", userId, "u.id=:userId");
  }

  public void setPartner(Long partnerId) {
    addCondition("partner", partnerId, "e.sight.partner.id=:partner");
  }

  public void setCity(String city) {
    if (city != null) {
      addCondition("city", WordUtils.capitalizeFully(city), "e.location.city=:city");
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

  public void setSameCategorySightEventIds(Set<Long> sameCategorySightEventIds) {
    setFetchCategories(true);
    setExcludedIds(sameCategorySightEventIds);
    if (sameCategorySightEventIds != null && !sameCategorySightEventIds.isEmpty()) {
      addCondition("sameCategorySightEventsIds", sameCategorySightEventIds,
          "sec.category.id in (select distinct category.id from SightEventCategory where sightEvent.id in (:sameCategorySightEventsIds))");
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
    this.excludedIds = ids;
    addCondition("ids", ids, "e.id not in (:ids)");
  }

  public Set<Long> getExcludedIds() {
    return this.excludedIds;
  }

  public void setFetchTags(boolean fetchTags) {
    this.fetchTags = fetchTags;
  }

  public boolean isFetchTags() {
    return fetchTags;
  }

  public LanguageVersion getLanguage() {
    return language;
  }

  public void setLanguage(LanguageVersion language) {
    this.language = language;
  }

  public Date getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Date getDateTo() {
    return dateTo;
  }

  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }

  public boolean isLoggedUserFavourites() {
    return loggedUserFavourites;
  }

  public void setLoggedUserFavourites() {
    this.loggedUserFavourites = true;
  }

}
