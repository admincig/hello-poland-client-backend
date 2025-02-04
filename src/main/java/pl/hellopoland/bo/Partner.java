package pl.hellopoland.bo;

import org.hibernate.Hibernate;
import pl.hellopoland.annotation.Multilingual;
import pl.hellopoland.enums.BusinessType;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.Translated;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Partner extends ModelSuperclass implements Translated, HptSubject {

  private static final long serialVersionUID = 6118414827783500940L;

  @NotNull
  @Column(nullable = false)
  private String name;

  @NotNull
  @Column(nullable = false)
  private String hptToken;

  private Long hptId;

  @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
  private List<User> users;

  @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
  private List<Sight> sight;

  @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
  private List<SightEvent> sightEvents;

  @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
  private List<Agreement> agreements;

  private BigDecimal commission;

  @Enumerated(EnumType.STRING)
  private BusinessType businessType;

  @NotNull
  @Column(nullable = false)
  private String email;

  private String affiliateCode;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
  private List<PartnerRepresentative> representatives;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, fetch = FetchType.LAZY)
  private Address address;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, fetch = FetchType.LAZY)
  private Address correspondenceAddress;

  private String bankAccount;

  @ManyToOne(fetch = FetchType.LAZY)
  private ContactPerson contactPerson;

  private String invoiceEmail;

  private String krs;

  private String taxNumber;

  private Long socialNumber;

  private String phone;

  private String regon;

  private String servicesDescription;

  private String shopUrl;

  @Multilingual
  private String description;

  private LocalDateTime created;

  @ManyToOne(fetch = FetchType.LAZY)
  private ContactPerson technicalContact;

  @ManyToOne(fetch = FetchType.LAZY)
  private ImageCollector mainImage;

  @Transient
  private List<Category> categories;

  @Transient
  private List<Tag> tags;

  @Transient
  private List<String> cities;

  @Transient
  private LanguageVersion currentLanguage;

  @NotNull
  @Column(length = 5, nullable = false)
  @Enumerated(EnumType.STRING)
  private LanguageVersion defaultLanguage = LanguageVersion.PL_PL;

  @Column(nullable = false)
  @ElementCollection
  @Enumerated(EnumType.STRING)
  private Set<LanguageVersion> availableLanguageVersions;

  private boolean blocked;

  public Long getHptId() {
    return hptId;
  }

  public void setHptId(Long hptId) {
    this.hptId = hptId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public List<Sight> getSight() {
    return sight;
  }

  public void setSight(List<Sight> sight) {
    this.sight = sight;
  }

  @Override
  public String getHptToken() {
    return hptToken;
  }

  public void setHptToken(String hptToken) {
    this.hptToken = hptToken;
  }

  public List<Agreement> getAgreements() {
    return agreements;
  }

  public void setAgreements(List<Agreement> agreements) {
    this.agreements = agreements;
  }

  public void addUser(User user) {
    if (this.users == null) {
      this.users = new ArrayList<>();
    }
    this.users.add(user);
  }

  public BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(BigDecimal commission) {
    this.commission = commission;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAffiliateCode() {
    return affiliateCode;
  }

  public void setAffiliateCode(String affiliateCode) {
    this.affiliateCode = affiliateCode;
  }


  public List<SightEvent> getSightEvents() {
    return sightEvents;
  }

  public void setSightEvents(List<SightEvent> sightEvents) {
    this.sightEvents = sightEvents;
  }

  public List<PartnerRepresentative> getRepresentatives() {
    return representatives;
  }

  public void setRepresentatives(List<PartnerRepresentative> representatives) {
    this.representatives = representatives;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Address getCorrespondenceAddress() {
    return correspondenceAddress;
  }

  public void setCorrespondenceAddress(Address correspondenceAddress) {
    this.correspondenceAddress = correspondenceAddress;
  }

  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }

  public ContactPerson getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(ContactPerson contactPerson) {
    this.contactPerson = contactPerson;
  }

  public String getInvoiceEmail() {
    return invoiceEmail;
  }

  public void setInvoiceEmail(String invoiceEmail) {
    this.invoiceEmail = invoiceEmail;
  }

  public String getKrs() {
    return krs;
  }

  public void setKrs(String krs) {
    this.krs = krs;
  }

  public String getTaxNumber() {
    return taxNumber;
  }

  public void setTaxNumber(String taxNumber) {
    this.taxNumber = taxNumber;
  }

  public Long getSocialNumber() {
    return socialNumber;
  }

  public void setSocialNumber(Long long1) {
    this.socialNumber = long1;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getRegon() {
    return regon;
  }

  public void setRegon(String regon) {
    this.regon = regon;
  }

  public String getServicesDescription() {
    return servicesDescription;
  }

  public void setServicesDescription(String servicesDescription) {
    this.servicesDescription = servicesDescription;
  }

  public String getShopUrl() {
    return shopUrl;
  }

  public void setShopUrl(String shopUrl) {
    this.shopUrl = shopUrl;
  }

  public ContactPerson getTechnicalContact() {
    return technicalContact;
  }

  public void setTechnicalContact(ContactPerson technicalContact) {
    this.technicalContact = technicalContact;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public ImageCollector getMainImage() {
    return mainImage;
  }

  public void setMainImage(ImageCollector mainImage) {
    this.mainImage = mainImage;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public List<String> getCities() {
    return cities;
  }

  public void setCities(List<String> cities) {
    this.cities = cities;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public BusinessType getBusinessType() {
    return businessType;
  }

  public void setBusinessType(BusinessType businessType) {
    this.businessType = businessType;
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

  public boolean isBlocked() {
    return blocked;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public void fetchRelations() {
    fetchSimpleRelations();
    fetchCollections();
  }

  private void fetchCollections() {
    Hibernate.initialize(this.getAgreements());
    Hibernate.initialize(this.getAvailableLanguageVersions());
    Hibernate.initialize(this.getRepresentatives());
    Hibernate.initialize(this.getUsers());
    Hibernate.initialize(this.getSightEvents());
    Hibernate.initialize(this.getSight());
    if (this.getSight() != null) {
      for (Sight sight : this.getSight()) {
        Hibernate.initialize(sight.getMainImage());
      }
    }
    if (this.getSightEvents() != null) {
      for (SightEvent sightEvent : this.getSightEvents()) {
        Hibernate.initialize(sightEvent.getMainImage());
      }
    }
  }

  private void fetchSimpleRelations() {
    Hibernate.initialize(this.getAddress());
    Hibernate.initialize(this.getCorrespondenceAddress());
    Hibernate.initialize(this.getContactPerson());
    Hibernate.initialize(this.getTechnicalContact());
    Hibernate.initialize(this.getMainImage());
  }
}
