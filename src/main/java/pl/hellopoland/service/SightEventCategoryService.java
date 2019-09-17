package pl.hellopoland.service;

import javax.ejb.Stateless;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;

@Stateless
public class SightEventCategoryService extends ServiceSuperclass {

  public SightEvent addCategory(SightEvent se, Category cat) {
    if (se.getCategories().add(cat)) {
      cat.setAssignedItemsCount(cat.getAssignedItemsCount() + 1);
    }
    em.flush();
    return se;
  }

  public SightEvent removeCategory(SightEvent se, Category cat) {
    if (se.getCategories().remove(cat)) {
      cat.setAssignedItemsCount(cat.getAssignedItemsCount() - 1);
    }
    em.flush();
    return se;
  }
}
