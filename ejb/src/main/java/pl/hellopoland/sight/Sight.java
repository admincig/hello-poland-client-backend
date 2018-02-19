package pl.hellopoland.sight;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.image.Image;

@Entity
public class Sight extends ModelSuperclass {
  private static final long serialVersionUID = -34796485244638912L;

  @NotNull
  private String name;
  @ManyToOne
  private Image mainImage;
  @OneToMany(mappedBy = "sight")
  private Collection<Ticket> tickets;
  @OneToMany(mappedBy = "sight")
  private Collection<Agreement> agreements;
  private String lead;
  @Column(columnDefinition = "text")
  private String description;
  private Integer minPrice;
  private Float score;
  @Embedded
  private SightLocation location;
  private String email;
  private String phone;
  @OneToMany(mappedBy = "sight")
  private Collection<OpeningHours> openingHours;
  @ManyToOne
  private Portal portal;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Image getMainImage() {
    return mainImage;
  }

  public void setMainImage(Image mainImage) {
    this.mainImage = mainImage;
  }

  public Collection<Ticket> getTickets() {
    return tickets;
  }

  public void setTickets(Collection<Ticket> tickets) {
    this.tickets = tickets;
  }

  public Collection<Agreement> getAgreements() {
    return agreements;
  }

  public void setAgreements(Collection<Agreement> agreements) {
    this.agreements = agreements;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(Integer minPrice) {
    this.minPrice = minPrice;
  }

  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public String getLead() {
    return lead;
  }

  public void setLead(String lead) {
    this.lead = lead;
  }

  public Collection<OpeningHours> getOpeningHours() {
    return openingHours;
  }

  public void setOpeningHours(Collection<OpeningHours> openingHours) {
    this.openingHours = openingHours;
  }

  public SightLocation getLocation() {
    return location;
  }

  public void setLocation(SightLocation location) {
    this.location = location;
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

  public Portal getPortal() {
    return portal;
  }

  public void setPortal(Portal portal) {
    this.portal = portal;
  }



}
