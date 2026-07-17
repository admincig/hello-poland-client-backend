package pl.hellopoland.bo;

import java.io.Serializable;
import java.util.Objects;

public class PromotionCampaignTagId implements Serializable {

  private static final long serialVersionUID = 1735310590643488729L;

  private Long promotionCampaign;
  private Long tag;

  public Long getPromotionCampaign() {
    return promotionCampaign;
  }

  public void setPromotionCampaign(Long promotionCampaign) {
    this.promotionCampaign = promotionCampaign;
  }

  public Long getTag() {
    return tag;
  }

  public void setTag(Long tag) {
    this.tag = tag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PromotionCampaignTagId)) {
      return false;
    }
    PromotionCampaignTagId that = (PromotionCampaignTagId) o;
    return Objects.equals(promotionCampaign, that.promotionCampaign)
        && Objects.equals(tag, that.tag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(promotionCampaign, tag);
  }
}
