package pl.hellopoland.bo;

import pl.hellopoland.enums.PromotionScopeType;
import pl.hellopoland.enums.PromotionStatus;
import pl.hellopoland.enums.PromotionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "promotion_campaign")
public class PromotionCampaign extends ModelSuperclass {

  private static final long serialVersionUID = 2312106818703767029L;

  @NotNull
  @Column(nullable = false)
  private String name;
  @NotNull
  @Column(name = "promotion_type", nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionType promotionType;
  @NotNull
  @Column(nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionStatus status = PromotionStatus.DRAFT;
  @NotNull
  @Column(name = "scope_type", nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionScopeType scopeType;
  @NotNull
  @Column(name = "valid_from", nullable = false)
  private Date validFrom;
  @NotNull
  @Column(name = "valid_to", nullable = false)
  private Date validTo;
  @Column(name = "global_limit")
  private Integer globalLimit;
  @Column(name = "code_limit")
  private Integer codeLimit;
  @Column(name = "customer_limit")
  private Integer customerLimit;
  @Column(name = "daily_limit")
  private Integer dailyLimit;
  @Column(name = "required_ticket_quantity")
  private Integer requiredTicketQuantity;
  @Column(name = "granted_ticket_quantity")
  private Integer grantedTicketQuantity;
  @NotNull
  @Column(name = "reserved_redemptions_count", nullable = false)
  private Integer reservedRedemptionsCount = 0;
  @NotNull
  @Column(name = "used_redemptions_count", nullable = false)
  private Integer usedRedemptionsCount = 0;
  @Column(name = "discount_percent")
  private BigDecimal discountPercent;
  @Column(name = "discount_amount_gross")
  private Integer discountAmountGross;
  @NotNull
  @Column(name = "created_at", nullable = false)
  private Date createdAt = new Date();
  @Column(name = "updated_at")
  private Date updatedAt;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "created_by")
  private User createdBy;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "updated_by")
  private User updatedBy;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "marker_tag_id")
  private Tag markerTag;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PromotionType getPromotionType() {
    return promotionType;
  }

  public void setPromotionType(PromotionType promotionType) {
    this.promotionType = promotionType;
  }

  public PromotionStatus getStatus() {
    return status;
  }

  public void setStatus(PromotionStatus status) {
    this.status = status;
  }

  public PromotionScopeType getScopeType() {
    return scopeType;
  }

  public void setScopeType(PromotionScopeType scopeType) {
    this.scopeType = scopeType;
  }

  public Date getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
  }

  public Date getValidTo() {
    return validTo;
  }

  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

  public Integer getGlobalLimit() {
    return globalLimit;
  }

  public void setGlobalLimit(Integer globalLimit) {
    this.globalLimit = globalLimit;
  }

  public Integer getCodeLimit() {
    return codeLimit;
  }

  public void setCodeLimit(Integer codeLimit) {
    this.codeLimit = codeLimit;
  }

  public Integer getCustomerLimit() {
    return customerLimit;
  }

  public void setCustomerLimit(Integer customerLimit) {
    this.customerLimit = customerLimit;
  }

  public Integer getDailyLimit() {
    return dailyLimit;
  }

  public void setDailyLimit(Integer dailyLimit) {
    this.dailyLimit = dailyLimit;
  }

  public Integer getRequiredTicketQuantity() {
    return requiredTicketQuantity;
  }

  public void setRequiredTicketQuantity(Integer requiredTicketQuantity) {
    this.requiredTicketQuantity = requiredTicketQuantity;
  }

  public Integer getGrantedTicketQuantity() {
    return grantedTicketQuantity;
  }

  public void setGrantedTicketQuantity(Integer grantedTicketQuantity) {
    this.grantedTicketQuantity = grantedTicketQuantity;
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

  public BigDecimal getDiscountPercent() {
    return discountPercent;
  }

  public void setDiscountPercent(BigDecimal discountPercent) {
    this.discountPercent = discountPercent;
  }

  public Integer getDiscountAmountGross() {
    return discountAmountGross;
  }

  public void setDiscountAmountGross(Integer discountAmountGross) {
    this.discountAmountGross = discountAmountGross;
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

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public User getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(User updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Tag getMarkerTag() {
    return markerTag;
  }

  public void setMarkerTag(Tag markerTag) {
    this.markerTag = markerTag;
  }
}
