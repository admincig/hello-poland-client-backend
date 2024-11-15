package pl.hellopoland.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class Agreement extends ModelSuperclass {

  private static final long serialVersionUID = -3524491821261836536L;

  @ManyToMany(mappedBy = "agreements")
  private Set<Sight> sight;

  @ManyToMany(mappedBy = "agreements")
  private Set<SightEvent> sightEvent;

  @ManyToOne(optional = false)
  private Partner partner;

  @NotBlank
  private String linkUrl;

  @NotBlank
  private String text;

  private boolean obligatory;

  public Set<Sight> getSight() {
    return sight;
  }

  public void setSight(Set<Sight> sight) {
    this.sight = sight;
  }

  public Set<SightEvent> getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(Set<SightEvent> sightEvent) {
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

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

}
