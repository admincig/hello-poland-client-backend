package pl.hellopoland.bo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.Imaged;
import pl.hellopoland.util.Located;

@Entity
public class SightEvent extends ModelSuperclass implements Located, Imaged {

  private static final long serialVersionUID = -34796485244638912L;

  @NotNull
  private String name;
  private Date date;
  private Boolean generalAdmission;
  @ManyToOne
  private ImageCollector mainImage;
  @OneToMany
  @JoinTable(name = "sightevent_images",
      joinColumns = {@JoinColumn(name = "sightevent_id", referencedColumnName = "id")},
      inverseJoinColumns = {
          @JoinColumn(name = "imagecollector_id", referencedColumnName = "id", unique = true)})
  private Collection<ImageCollector> images;
  @OneToMany(mappedBy = "sightEvent")
  private Collection<TicketDefinition> tickets;
  @ManyToMany
  private Set<Agreement> agreements;
  private String lead;
  @Column(columnDefinition = "varchar(2500)")
  private String description;
  private Integer duration;
  private Integer minPrice;
  private Float score;
  @Embedded
  private Location location;
  private String email;
  private String phone;
  @ManyToOne
  private FileDescriptor pdfAttachment;
  @OneToMany(mappedBy = "sightEvent")
  private Collection<OpeningHours> openingHours;
  @ManyToOne
  private Portal portal;
  @ManyToOne(fetch = FetchType.EAGER)
  private Sight sight;
  @ManyToOne
  private Partner partner;
  @NotNull
  private Long hptId;
  private boolean active = true;
  private boolean published;
  private boolean blocked;
  @NotNull
  @Column(length = 5, nullable = false)
  @Enumerated(EnumType.STRING)
  private LanguageVersion defaultLanguage;
  @NotNull
  @Column(nullable = false)
  @ElementCollection
  @Enumerated(EnumType.STRING)
  private Set<LanguageVersion> availableLanguageVersions;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public ImageCollector getMainImage() {
    return mainImage;
  }

  @Override
  public void setMainImage(ImageCollector mainImage) {
    this.mainImage = mainImage;
  }

  public Collection<TicketDefinition> getTickets() {
    return tickets;
  }

  public void setTickets(Collection<TicketDefinition> tickets) {
    this.tickets = tickets;
  }

  public Collection<Agreement> getAgreements() {
    return agreements;
  }

  public void setAgreements(Set<Agreement> agreements) {
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

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void setLocation(Location location) {
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
    float score = (float) (4.8 + new Random().nextDouble() / 5);
    this.score = new BigDecimal(score).setScale(1, RoundingMode.HALF_UP).floatValue();
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

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Long getHptId() {
    return hptId;
  }

  public void setHptId(Long hptId) {
    this.hptId = hptId;
  }

  public Boolean getGeneralAdmission() {
    return generalAdmission;
  }

  public void setGeneralAdmission(Boolean generalAdmission) {
    this.generalAdmission = generalAdmission;
  }

  public Collection<ImageCollector> getImages() {
    return images;
  }

  public void setImages(Collection<ImageCollector> images) {
    this.images = images;
  }


  public FileDescriptor getPdfAttachment() {
    return pdfAttachment;
  }

  public void setPdfAttachment(FileDescriptor pdfAttachment) {
    this.pdfAttachment = pdfAttachment;
  }

  public void addImage(ImageCollector img) {
    if (images == null) {
      images = new ArrayList<>();
    }
    images.add(img);
  }

  public void removeImage(ImageCollector img) {
    if (images != null) {
      images.remove(img);
    }
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public LanguageVersion getDefaultLanguage() {
    return defaultLanguage;
  }

  public void setDefaultLanguage(LanguageVersion defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  public Set<LanguageVersion> getAvailableLanguageVersions() {
    return availableLanguageVersions;
  }

  public void setAvailableLanguageVersions(Set<LanguageVersion> availableLanguageVersions) {
    this.availableLanguageVersions = availableLanguageVersions;
  }

}
