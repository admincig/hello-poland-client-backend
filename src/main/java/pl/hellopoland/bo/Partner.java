package pl.hellopoland.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import pl.hellopoland.soap.p24.enums.BusinessType;
import pl.hellopoland.soap.p24.enums.Trade;
import pl.hellopoland.util.Located;

@Entity
public class Partner extends ModelSuperclass implements Located {

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

  private BusinessType businessType;

  private Trade trade;

  private List<PartnerRepresentative> representatives;

  private

  private Location location;

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

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public void setLocation(Location location) {
    this.location = location;
  }

}
