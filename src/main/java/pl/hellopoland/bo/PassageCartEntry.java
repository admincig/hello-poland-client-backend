package pl.hellopoland.bo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class PassageCartEntry extends ModelSuperclass {
  private static final long serialVersionUID = 3931540543549036467L;
  @NotNull
  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  private PassageCart passageCart;
  @NotNull
  @Column(nullable = false)
  private String description;
  @NotNull
  @Column(nullable = false)
  private String name;
  @NotNull
  @Column(nullable = false)
  private Long number;
  @NotNull
  @Column(nullable = false)
  private Integer price;
  @NotNull
  @Column(nullable = false)
  private Integer quantity;
  @NotNull
  @Column(nullable = false)
  private Integer targetAmount;
  @NotNull
  @Column(nullable = false)
  private Integer targetPosId;
  @NotNull
  @Column(nullable = false)
  private BigDecimal commission;

  public PassageCartEntry() {}

  public PassageCartEntry(OrderEntry oe) {
    this.name = oe.getName();
    this.number = oe.getExternalId();
    this.quantity = 1;
    setTargetAmountAndCommission(oe);
    this.price = this.targetAmount;
    this.targetPosId = oe.getDateEntry().getSightEntry().getSightEvent().getPartner().getP24Id();
  }

  private void setTargetAmountAndCommission(OrderEntry oe) {
    var totalOriginal = new BigDecimal(oe.getUnitPrice() * oe.getQuantity());
    var totalDiscounted = new BigDecimal(oe.getRealPrice() * oe.getQuantity());
    var hundred = new BigDecimal("100");
    var hplPartMultiplied = BigDecimal.ZERO;
    if (oe.getDiscount() != null) {
      hplPartMultiplied = new BigDecimal(oe.getQuantity() * oe.getDiscount().getHplPart());
    }
    this.commission = totalOriginal
        .multiply(oe.getDateEntry().getSightEntry().getSightEvent().getPartner().getCommission())
        .divide(hundred)
        .subtract(hplPartMultiplied).max(BigDecimal.ZERO);
    this.targetAmount =
        totalDiscounted.subtract(this.commission)
            .setScale(0, RoundingMode.HALF_EVEN).intValue();
  }

  public PassageCart getPassageCart() {
    return passageCart;
  }

  public void setPassageCart(PassageCart passageCart) {
    this.passageCart = passageCart;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Integer getTargetAmount() {
    return targetAmount;
  }

  public void setTargetAmount(Integer targetAmount) {
    this.targetAmount = targetAmount;
  }

  public Integer getTargetPosId() {
    return targetPosId;
  }

  public void setTargetPosId(Integer targetPosId) {
    this.targetPosId = targetPosId;
  }

  public BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(BigDecimal commission) {
    this.commission = commission;
  }

}
