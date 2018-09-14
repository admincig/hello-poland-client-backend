package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Agreement extends ModelSuperclass {

  private static final long serialVersionUID = -3524491821261836536L;

  @ManyToOne
  private Sight sight;

  @ManyToOne
  private SightEvent sightEvent;

  @ManyToOne(optional = false)
  private Partner partner;

  @NotBlank
  private String linkUrl;

  @NotBlank
  private String text;

  private boolean obligatory;

  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public boolean isObligatory() {
    return obligatory;
  }

  public void setObligatory(boolean obligatory) {
    this.obligatory = obligatory;
  }

  public Sight getSight() {
    return sight;
  }

  public void setSight(Sight sight) {
    this.sight = sight;
  }

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

}
