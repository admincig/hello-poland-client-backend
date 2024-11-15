package pl.hellopoland.service;

import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;

import jakarta.ejb.Stateless;

@Stateless
public class SightEventTagService extends ServiceSuperclass {

  public SightEvent addTag(SightEvent se, Tag tag) {
    if (se.getTags().stream().filter(t -> tag.getId().equals(t.getTag().getId()))
        .findFirst().isEmpty()) {
      SightEventTag set = new SightEventTag();
      set.setTag(tag);
      set.setSightEvent(se);
      em.persist(set);
      se.getTags().add(set);
      tag.setAssignedItemsCount(tag.getAssignedItemsCount() + 1);
      em.flush();
    }
    return se;
  }

  public SightEvent removeTag(SightEvent se, Tag tag) {
    se.getTags().stream().filter(t -> tag.getId().equals(t.getTag().getId()))
        .findFirst().ifPresent(set -> {
          se.getTags().remove(set);
          em.remove(set);
          tag.setAssignedItemsCount(tag.getAssignedItemsCount() - 1);
          em.flush();
        });
    return se;
  }
}
