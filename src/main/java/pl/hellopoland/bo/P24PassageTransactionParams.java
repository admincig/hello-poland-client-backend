package pl.hellopoland.bo;

import java.util.Collection;

public class P24PassageTransactionParams {
  private String address;
  private Integer amount;
  private String city;
  private String client;
  private String country;
  private String crc;
  private String currency;
  private String description;
  private String email;
  private String language;
  private Integer merchantId;
  private String phone;
  private String sessionId;
  private String urlStatus;
  private String zip;
  private Collection<P24PassageCartEntry> passageCart;

  public P24PassageTransactionParams() {}

  public P24PassageTransactionParams(Order o) {
    OrderDetails od = o.getDetails();
    this.address = "";
    this.city = od.getCity();
    this.client = od.getFirstName() + " " + od.getLastName();
    this.country = od.getCountry();
    this.currency = "PLN";
    this.email = od.getEmail();
    this.language = "pl";
    this.phone = od.getPhone();
    this.sessionId = o.getHash();
    this.zip = "";
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCrc() {
    return crc;
  }

  public void setCrc(String crc) {
    this.crc = crc;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Integer getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(Integer merchantId) {
    this.merchantId = merchantId;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getUrlStatus() {
    return urlStatus;
  }

  public void setUrlStatus(String urlStatus) {
    this.urlStatus = urlStatus;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public Collection<P24PassageCartEntry> getPassageCart() {
    return passageCart;
  }

  public void setPassageCart(Collection<P24PassageCartEntry> passageCart) {
    this.passageCart = passageCart;
  }

}
