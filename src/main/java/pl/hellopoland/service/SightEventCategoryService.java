package pl.hellopoland.service;

import javax.ejb.Stateless;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;

@Stateless
public class SightEventCategoryService {

  public SightEvent addCategory(SightEvent se, Category cat) {
    se.getCategories().add(cat);
    return se;
  }

  public SightEvent removeCategory(SightEvent se, Category cat) {
    se.getCategories().remove(cat);
    return se;
  }
}
