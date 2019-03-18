package pl.hellopoland.bo;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class PassageCart extends ModelSuperclass {
  private static final long serialVersionUID = -7093580946179485807L;

  @NotNull
  @OneToOne(optional = false)
  private Order order;
  @NotNull
  @Column(nullable = false)
  private boolean sandbox;
  @NotNull
  @Column(nullable = false)
  private Integer amount;
  @NotNull
  @Column(nullable = false)
  private String country;
  @NotNull
  @Column(nullable = false)
  private String currency;
  @NotNull
  @Column(nullable = false)
  private String description;
  @NotNull
  @Column(nullable = false)
  private String language;
  @NotNull
  @Column(nullable = false)
  private Integer merchantId;
  @NotNull
  @Column(nullable = false)
  private String sign;
  @NotNull
  @Column(nullable = false)
  private String urlStatus;
  @NotNull
  @Column(nullable = false)
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "passageCart")
  private Set<PassageCartEntry> cartEntries;
  @NotNull
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "passageCart", optional = false)
  private PassageCartEntry hpCommissionEntry;

  public PassageCart() {}

  public PassageCart(Order order) {
    this.order = order;
  }

  public boolean isSandbox() {
    return sandbox;
  }

  public void setSandbox(boolean sandbox) {
    this.sandbox = sandbox;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
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

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getUrlStatus() {
    return urlStatus;
  }

  public void setUrlStatus(String urlStatus) {
    this.urlStatus = urlStatus;
  }

  public Set<PassageCartEntry> getCartEntries() {
    return cartEntries;
  }

  public void setCartEntries(Set<PassageCartEntry> cartEntries) {
    this.cartEntries = cartEntries;
  }

  public PassageCartEntry getHpCommissionEntry() {
    return hpCommissionEntry;
  }

  public void setHpCommissionEntry(PassageCartEntry hpCommissionEntry) {
    this.hpCommissionEntry = hpCommissionEntry;
  }

}
