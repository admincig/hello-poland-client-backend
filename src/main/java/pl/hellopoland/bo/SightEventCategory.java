package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class SightEventCategory extends ModelSuperclass {
  private static final long serialVersionUID = -4535085232781196234L;

  @NotNull
  @ManyToOne(optional = false)
  private SightEvent sightEvent;
  @NotNull
  @ManyToOne(optional = false)
  private Category category;

  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

}
