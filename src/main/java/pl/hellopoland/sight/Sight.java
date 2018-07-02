package pl.hellopoland.sight;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.image.Image;
import pl.hellopoland.partner.Partner;

@Entity
public class Sight extends ModelSuperclass {

  private static final long serialVersionUID = -6821312294116712881L;

  @Column
  private String name;

  private String lead;

  private String description;

  @ManyToOne
  private Image mainImage;

  private String email;

  private String phone;

  @Embedded
  private SightLocation sightLocation;

  @ManyToOne
  private Partner partner;

  private boolean active = true;

  @OneToMany(mappedBy = "sight")
  private List<SightEvent> sightEvents;

  public Sight() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLead() {
    return lead;
  }

  public void setLead(String lead) {
    this.lead = lead;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Image getMainImage() {
    return mainImage;
  }

  public void setMainImage(Image mainImage) {
    this.mainImage = mainImage;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public SightLocation getSightLocation() {
    return sightLocation;
  }

  public void setSightLocation(SightLocation sightLocation) {
    this.sightLocation = sightLocation;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Partner getPartner() {
    return partner;
  }

  public void setPartner(Partner partner) {
    this.partner = partner;
  }

  public List<SightEvent> getSightEvents() {
    return sightEvents;
  }

  public void setSightEvents(List<SightEvent> sightEvents) {
    this.sightEvents = sightEvents;
  }
}
