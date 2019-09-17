package pl.hellopoland.service;

import javax.ejb.Stateless;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;

@Stateless
public class SightEventCategoryService extends ServiceSuperclass {

  public SightEvent addCategory(SightEvent se, Category cat) {
    if (se.getCategories().stream().filter(cat::equals)
        .findFirst().isEmpty()) {
      SightEventCategory sec = new SightEventCategory();
      sec.setCategory(cat);
      sec.setSightEvent(se);
      em.persist(sec);
      se.getCategories().add(sec);
      cat.setAssignedItemsCount(cat.getAssignedItemsCount() + 1);
      em.flush();
    }
    return se;
  }

  public SightEvent removeCategory(SightEvent se, Category cat) {
    se.getCategories().stream().filter(cat::equals)
        .findFirst().ifPresent(sec -> {
          se.getCategories().remove(sec);
          em.remove(sec);
          cat.setAssignedItemsCount(cat.getAssignedItemsCount() - 1);
          em.flush();
        });
    return se;
  }
}
