package pl.hellopoland.config;

import pl.hellopoland.bo.Sight;

import java.util.Set;

public class SightPagedCollectionConfig extends PagedCollectionConfig<Sight> {

  private boolean currentPartner;
  private boolean fetchSightEvents;
  private boolean fetchUsers;

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
        + "join fetch e.partner p "
        + "left join fetch p.contactPerson pc "
        + "left join fetch p.technicalContact tc"
        + (fetchSightEvents ? " left join fetch e.sightEvents ses left join fetch ses.mainImage sesMis" : "")
        + (fetchUsers ? " inner join e.users u" : "");
  }

  public void setSearchQuery(String searchQuery) {
    if (searchQuery != null) {
      addCondition("searchQuery",
          searchQuery, "tsearch('polish_hunspell', e.searchIndex, :searchQuery) = true");
      /*
       * "%" + searchQuery.toLowerCase() + "%",
       * "((unaccent(lower(e.name)) like unaccent(:searchQuery))" +
       * " or (unaccent(lower(e.lead)) like unaccent(:searchQuery))" +
       * " or (unaccent(lower(e.location.city)) like unaccent(:searchQuery)))");
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
    addCondition("pBlocked", false, "e.partner.blocked=:pBlocked");
  }

  public void onlyFavourite(Long userId) {
    fetchUsers = true;
    addCondition("userId", userId, "u.id=:userId");
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

  public void fetchSightEvents(boolean fetchSightEvents) {
    this.fetchSightEvents = fetchSightEvents;
  }

  public boolean isFetchSightEvents() {
    return fetchSightEvents;
  }

  public void setFetchSightEvents(boolean fetchSightEvents) {
    this.fetchSightEvents = fetchSightEvents;
  }

}
