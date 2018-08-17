package pl.hellopoland.bo;

public class P24PassageCartEntry {
  private String posId;
  private Integer amount;

  public P24PassageCartEntry(String posId, Integer amount) {
    this.posId = posId;
    this.amount = amount;
  }

  public String getPosId() {
    return posId;
  }

  public void setPosId(String posId) {
    this.posId = posId;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }
}
