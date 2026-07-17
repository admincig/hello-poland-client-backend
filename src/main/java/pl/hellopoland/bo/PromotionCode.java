package pl.hellopoland.bo;

import pl.hellopoland.enums.PromotionCodeStatus;
import pl.hellopoland.enums.PromotionCodeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "promotion_code")
public class PromotionCode extends ModelSuperclass {

  private static final long serialVersionUID = -3327423130095962301L;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_campaign_id", nullable = false)
  private PromotionCampaign promotionCampaign;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_code_batch_id")
  private PromotionCodeBatch promotionCodeBatch;
  @NotNull
  @Column(nullable = false, unique = true)
  private String code;
  @NotNull
  @Column(name = "code_type", nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionCodeType codeType;
  @NotNull
  @Column(nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionCodeStatus status = PromotionCodeStatus.ACTIVE;
  @Column(name = "max_redemptions")
  private Integer maxRedemptions;
  @Column(name = "max_redemptions_per_customer")
  private Integer maxRedemptionsPerCustomer;
  @Column(name = "max_redemptions_per_day")
  private Integer maxRedemptionsPerDay;
  @NotNull
  @Column(name = "reserved_redemptions_count", nullable = false)
  private Integer reservedRedemptionsCount = 0;
  @NotNull
  @Column(name = "used_redemptions_count", nullable = false)
  private Integer usedRedemptionsCount = 0;
  @Column(name = "reserved_until")
  private Date reservedUntil;
  @NotNull
  @Column(name = "created_at", nullable = false)
  private Date createdAt = new Date();
  @Column(name = "updated_at")
  private Date updatedAt;
  @Column(name = "disabled_at")
  private Date disabledAt;

  public PromotionCampaign getPromotionCampaign() {
    return promotionCampaign;
  }

  public void setPromotionCampaign(PromotionCampaign promotionCampaign) {
    this.promotionCampaign = promotionCampaign;
  }

  public PromotionCodeBatch getPromotionCodeBatch() {
    return promotionCodeBatch;
  }

  public void setPromotionCodeBatch(PromotionCodeBatch promotionCodeBatch) {
    this.promotionCodeBatch = promotionCodeBatch;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public PromotionCodeType getCodeType() {
    return codeType;
  }

  public void setCodeType(PromotionCodeType codeType) {
    this.codeType = codeType;
  }

  public PromotionCodeStatus getStatus() {
    return status;
  }

  public void setStatus(PromotionCodeStatus status) {
    this.status = status;
  }

  public Integer getMaxRedemptions() {
    return maxRedemptions;
  }

  public void setMaxRedemptions(Integer maxRedemptions) {
    this.maxRedemptions = maxRedemptions;
  }

  public Integer getMaxRedemptionsPerCustomer() {
    return maxRedemptionsPerCustomer;
  }

  public void setMaxRedemptionsPerCustomer(Integer maxRedemptionsPerCustomer) {
    this.maxRedemptionsPerCustomer = maxRedemptionsPerCustomer;
  }

  public Integer getMaxRedemptionsPerDay() {
    return maxRedemptionsPerDay;
  }

  public void setMaxRedemptionsPerDay(Integer maxRedemptionsPerDay) {
    this.maxRedemptionsPerDay = maxRedemptionsPerDay;
  }

  public Integer getReservedRedemptionsCount() {
    return reservedRedemptionsCount;
  }

  public void setReservedRedemptionsCount(Integer reservedRedemptionsCount) {
    this.reservedRedemptionsCount = reservedRedemptionsCount;
  }

  public Integer getUsedRedemptionsCount() {
    return usedRedemptionsCount;
  }

  public void setUsedRedemptionsCount(Integer usedRedemptionsCount) {
    this.usedRedemptionsCount = usedRedemptionsCount;
  }

  public Date getReservedUntil() {
    return reservedUntil;
  }

  public void setReservedUntil(Date reservedUntil) {
    this.reservedUntil = reservedUntil;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Date getDisabledAt() {
    return disabledAt;
  }

  public void setDisabledAt(Date disabledAt) {
    this.disabledAt = disabledAt;
  }
}
