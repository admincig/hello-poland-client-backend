package pl.hellopoland.config;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import pl.hellopoland.bo.SightEvent;

public class SightEventPagedCollectionConfig extends PagedCollectionConfig<SightEvent> {

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
    addCondition("name", name.toLowerCase(), "lower(e.name)=:name");
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

  public void setDate(Date date) {
    var startDay = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()).atStartOfDay();
    var endDay = startDay.plusDays(1);
    addCondition("startDay", startDay, "e.date >= :startDay");
    addCondition("endDay", endDay, "e.date < :endDay");
  }

  public void setCity(String city) {
    addCondition("city", city, "e.location.city");
  }

}
