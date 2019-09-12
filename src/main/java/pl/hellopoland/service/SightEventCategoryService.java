package pl.hellopoland.service;

import javax.ejb.Stateless;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;

@Stateless
public class SightEventCategoryService extends ServiceSuperclass {

  public SightEvent addCategory(SightEvent se, Category cat) {
    se.getCategories().add(cat);
    em.flush();
    return se;
  }

  public SightEvent removeCategory(SightEvent se, Category cat) {
    se.getCategories().remove(cat);
    em.flush();
    return se;
  }
}
