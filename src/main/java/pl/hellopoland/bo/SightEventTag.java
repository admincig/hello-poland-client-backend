package pl.hellopoland.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class SightEventTag extends ModelSuperclass {
  private static final long serialVersionUID = -4535085232781196234L;

  @NotNull
  @ManyToOne(optional = false)
  private SightEvent sightEvent;
  @NotNull
  @ManyToOne(optional = false)
  private Tag tag;

  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }

}
