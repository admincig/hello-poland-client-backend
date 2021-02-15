package pl.hellopoland.bo;

import pl.hellopoland.annotation.Multilingual;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.Imaged;
import pl.hellopoland.util.Located;
import pl.hellopoland.util.Partnered;
import pl.hellopoland.util.Translated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Sight extends ModelSuperclass implements Located, Imaged, Translated, Partnered {

  private static final long serialVersionUID = -6821312294116712881L;

  @Multilingual
  private String name;
  @Multilingual
  private String lead;
  @Column(columnDefinition = "varchar(2500)")
  @Multilingual
  private String description;
  private Float score;
  @ManyToOne
  private ImageCollector mainImage;
  @OneToMany
  @JoinTable(name = "sight_images",
      joinColumns = {@JoinColumn(name = "sight_id", referencedColumnName = "id")},
      inverseJoinColumns = {
          @JoinColumn(name = "imagecollector_id", referencedColumnName = "id", unique = true)})
  private List<ImageCollector> images = new ArrayList<>();
  private String email;
  private String phone;
  @Embedded
  private Location location;
  @ManyToOne
  private Partner partner;
  private boolean active = true;
  @OneToMany(mappedBy = "sight")
  private List<SightEvent> sightEvents = new ArrayList<>();
  @OneToMany(mappedBy = "sight")
  @OrderBy("day asc")
  private List<OpeningHours> openingHours = new ArrayList<>();
  @ManyToMany
  private Set<Agreement> agreements = new HashSet<>();
  private boolean published;
  private boolean blocked;
  @NotNull
  @Column(length = 5, nullable = false)
  @Enumerated(EnumType.STRING)
  private LanguageVersion defaultLanguage;
  @Column(nullable = false)
  @ElementCollection
  @Enumerated(EnumType.STRING)
  private Set<LanguageVersion> availableLanguageVersions;
  @Transient
  private LanguageVersion currentLanguage;
  @Column(columnDefinition = "varchar")
  private String searchIndex;
  @Transient
  private Set<Category> categories;
  @Transient
  private Set<Tag> tags;
  @ManyToMany
  private Set<User> users = new HashSet<>();
  @Transient
  private boolean favourite;

  public Sight() {}

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

  @Override
  public ImageCollector getMainImage() {
    return mainImage;
  }

  @Override
  public void setMainImage(ImageCollector mainImage) {
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

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void setLocation(Location sightLocation) {
    this.location = sightLocation;
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

  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public void generateRandomScore() {
    float score = (float) (4.8 + new Random().nextDouble() / 5);
    this.score = new BigDecimal(score).setScale(1, RoundingMode.HALF_UP).floatValue();
  }

  public List<ImageCollector> getImages() {
    return images;
  }

  public void setImages(List<ImageCollector> images) {
    this.images = images;
  }

  public List<OpeningHours> getOpeningHours() {
    return openingHours;
  }

  public void setOpeningHours(List<OpeningHours> openingHours) {
    this.openingHours = openingHours;
  }

  public Collection<Agreement> getAgreements() {
    return agreements;
  }

  public void setAgreements(Set<Agreement> agreements) {
    this.agreements = agreements;
  }

  /**
   * Check whether this Sight is active, published and not blocked.
   * 
   * @return boolean
   */
  public boolean isAccessible() {
    return published && active && !blocked;
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

  @Override
  public LanguageVersion getDefaultLanguage() {
    return defaultLanguage;
  }

  @Override
  public void setDefaultLanguage(LanguageVersion defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

  @Override
  public Set<LanguageVersion> getAvailableLanguageVersions() {
    return availableLanguageVersions;
  }

  @Override
  public void setAvailableLanguageVersions(Set<LanguageVersion> availableLanguageVersions) {
    this.availableLanguageVersions = availableLanguageVersions;
  }

  @Override
  public boolean addAvailableLanguageVersion(LanguageVersion languageVersion) {
    if (availableLanguageVersions == null) {
      availableLanguageVersions = new HashSet<>();
    }
    return availableLanguageVersions.add(languageVersion);
  }

  @Override
  public boolean deleteAvailableLanguageVersion(LanguageVersion languageVersion) {
    if (availableLanguageVersions == null) {
      availableLanguageVersions = new HashSet<>();
    }
    return availableLanguageVersions.remove(languageVersion);
  }

  @Override
  public LanguageVersion getCurrentLanguage() {
    return currentLanguage;
  }

  @Override
  public void setCurrentLanguage(LanguageVersion currentLanguage) {
    this.currentLanguage = currentLanguage;
  }

  public String getSearchIndex() {
    return searchIndex;
  }

  public void setSearchIndex(String searchIndex) {
    this.searchIndex = searchIndex;
  }

  public void recreateSearchIndex(Set<String> words) {
    this.searchIndex =
        Stream.concat(
            Stream.of(email, name, phone),
            words.stream())
            .filter(Objects::nonNull)
            .flatMap(s -> Stream.of(s.split(" ")))
            .map(w -> w.replaceAll("[^a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]", ""))
            .distinct()
            .filter(w -> !w.isBlank())
            .collect(Collectors.joining(","));
  }

  public Set<Category> getCategories() {
    return categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void addUser(User user) {
    this.users.add(user);
  }

  public void removeUser(User user) {
    this.users.remove(user);
  }

  public boolean isFavourite() {
    return favourite;
  }

  public void setFavourite(boolean favourite) {
    this.favourite = favourite;
  }

  public void fetchCollections() {
    Optional.ofNullable(this.getAvailableLanguageVersions())
        .ifPresent(Collection::size);
    Optional.ofNullable(this.getAgreements()).ifPresent(Collection::size);
    Optional.ofNullable(this.getImages()).ifPresent(Collection::size);
    Optional.ofNullable(this.getOpeningHours()).ifPresent(Collection::size);
  }

}
