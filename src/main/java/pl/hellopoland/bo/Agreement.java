package pl.hellopoland.bo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Agreement extends ModelSuperclass {

  private static final long serialVersionUID = -3524491821261836536L;

  @ManyToOne(optional = false)
  private SightEvent sightEvent;
  private String linkText = "regulamin";
  private String linkUrl;
  private String text = "Akceptuję {link} obiektu";
  private boolean obligatory;


  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public String getLinkText() {
    return linkText;
  }

  public void setLinkText(String linkText) {
    this.linkText = linkText;
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

}
