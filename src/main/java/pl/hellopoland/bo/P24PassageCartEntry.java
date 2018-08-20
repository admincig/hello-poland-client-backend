package pl.hellopoland.bo;

public class P24PassageCartEntry {
  private String description;
  private String name;
  private Long number;
  private Integer price;
  private Integer quantity;
  private Integer targetAmount;
  private Integer targetPosId;

  public P24PassageCartEntry() {}

  public P24PassageCartEntry(OrderEntry oe) {
    // this.description = description;
    this.name = oe.getName();
    this.number = oe.getExternalId();
    this.price = oe.getUnitPrice();
    this.quantity = oe.getQuantity();
    this.targetAmount = oe.getUnitPrice() * oe.getQuantity();
    this.targetPosId = oe.getDateEntry().getSightEntry().getSightEvent().getPartner().getP24Id();
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

  public void setNumber(Long long1) {
    this.number = long1;
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
}
