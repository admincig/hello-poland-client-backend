package pl.hellopoland.bo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import pl.hellopoland.soap.p24.enums.BusinessType;
import pl.hellopoland.soap.p24.enums.Trade;

@Entity
public class Partner extends ModelSuperclass {

  private static final long serialVersionUID = 6118414827783500940L;

  @NotNull
  @Column(nullable = false)
  private Integer p24Id;

  @NotNull
  @Column(nullable = false)
  private String name;

  @NotNull
  @Column(nullable = false)
  private String hptToken;

  @OneToMany(mappedBy = "partner")
  private List<User> users;

  @OneToMany(mappedBy = "partner")
  private List<Sight> sight;

  @OneToMany(mappedBy = "partner")
  private List<SightEvent> sightEvents;

  @OneToMany(mappedBy = "partner")
  private List<Agreement> agreements;

  @NotNull
  @Column(nullable = false)
  private BigDecimal commission;

  @NotNull
  @Column(nullable = false)
  private String email;

  private String affiliateCode;

  @Enumerated(EnumType.STRING)
  private BusinessType businessType;

  @Enumerated(EnumType.STRING)
  private Trade trade;

  @ManyToMany(mappedBy = "partners")
  private List<PartnerRepresentative> representatives;

  @OneToOne(cascade = CascadeType.PERSIST)
  private Address address;

  @OneToOne(cascade = CascadeType.PERSIST)
  private Address correspondenceAddress;

  private String bankAccount;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private ContactPerson contactPerson;

  private String invoiceEmail;

  private String krs;

  private String taxNumber;

  private Integer socialNumber;

  private String phone;

  private String regon;

  private String servicesDescription;

  private String shopUrl;

  private LocalDateTime created;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private ContactPerson technicalContact;

  public Integer getP24Id() {
    return p24Id;
  }

  public void setP24Id(Integer p24Id) {
    this.p24Id = p24Id;
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

  public BusinessType getBusinessType() {
    return businessType;
  }

  public void setBusinessType(BusinessType businessType) {
    this.businessType = businessType;
  }

  public Trade getTrade() {
    return trade;
  }

  public void setTrade(Trade trade) {
    this.trade = trade;
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

  public Integer getSocialNumber() {
    return socialNumber;
  }

  public void setSocialNumber(Integer socialNumber) {
    this.socialNumber = socialNumber;
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

}
