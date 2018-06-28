package pl.hellopoland.sight;

import java.util.Collection;
import java.util.Date;
import java.util.Random;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import pl.hellopoland.ModelSuperclass;
import pl.hellopoland.image.Image;
import pl.hellopoland.partner.Partner;

@Entity
public class SightEvent extends ModelSuperclass {

  private static final long serialVersionUID = -34796485244638912L;

  @NotNull
  private String name;

  private Date date;

  private Integer availableTicketsNumber;

  @ManyToOne
  private Image mainImage;

  @OneToMany(mappedBy = "sightEvent")
  private Collection<Ticket> tickets;

  @OneToMany(mappedBy = "sightEvent")
  private Collection<Agreement> agreements;

  private String lead;

  @Column(columnDefinition = "text")
  private String description;

  private Integer duration;

  private Integer minPrice;

  private Float score;

  @Embedded
  private SightLocation location;

  private String email;

  private String phone;

  @OneToMany(mappedBy = "sightEvent")
  private Collection<OpeningHours> openingHours;

  @ManyToOne
  private Portal portal;

  @ManyToOne
  private Sight sight;

  @ManyToOne
  private Partner partner;

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

  public void generateRandomScore() {
    this.setScore((float) (4.8 + new Random().nextDouble() / 5));
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getAvailableTicketsNumber() {
    return availableTicketsNumber;
  }

  public void setAvailableTicketsNumber(Integer availableTicketsNumber) {
    this.availableTicketsNumber = availableTicketsNumber;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }
}
