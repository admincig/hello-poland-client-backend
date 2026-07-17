package pl.hellopoland.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "promotion_campaign_tag")
@IdClass(PromotionCampaignTagId.class)
public class PromotionCampaignTag implements Serializable {

  private static final long serialVersionUID = -3267056523699689393L;

  @Id
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_campaign_id", nullable = false)
  private PromotionCampaign promotionCampaign;
  @Id
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id", nullable = false)
  private Tag tag;
  @NotNull
  @Column(nullable = false)
  private boolean active = true;
  @NotNull
  @Column(name = "created_at", nullable = false)
  private Date createdAt = new Date();

  public PromotionCampaign getPromotionCampaign() {
    return promotionCampaign;
  }

  public void setPromotionCampaign(PromotionCampaign promotionCampaign) {
    this.promotionCampaign = promotionCampaign;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
