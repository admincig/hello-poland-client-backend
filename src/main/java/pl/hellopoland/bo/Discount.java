package pl.hellopoland.bo;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class Discount implements Serializable {
  private static final long serialVersionUID = 38141309801228584L;

  private Integer price;
  private Integer hplPart;
  private Integer partnerPart;


  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getHplPart() {
    return hplPart;
  }

  public void setHplPart(Integer hplPart) {
    this.hplPart = hplPart;
  }

  public Integer getPartnerPart() {
    return partnerPart;
  }

  public void setPartnerPart(Integer partnerPart) {
    this.partnerPart = partnerPart;
  }

}
