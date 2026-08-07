package pl.hellopoland.config;

import pl.hellopoland.bo.Partner;

import java.util.Set;

public class PartnerPagedCollectionConfig extends PagedCollectionConfig<Partner> {

  @Override
  public String joins() {
    return "left join fetch e.mainImage mi left join fetch e.address a";
  }

  public void setBlocked(boolean blocked) {
    addCondition("blocked", blocked, "e.blocked=:blocked");
  }

  public void setIds(Set<Long> ids) {
    addCondition("ids", ids == null || ids.isEmpty() ? Set.of(-1L) : ids, "e.id in (:ids)");
  }
}
