package pl.hellopoland.bo;

import pl.hellopoland.enums.PromotionCodeRedemptionStatus;
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
@Table(name = "promotion_code_redemption")
public class PromotionCodeRedemption extends ModelSuperclass {

  private static final long serialVersionUID = -5813096675680848022L;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_campaign_id", nullable = false)
  private PromotionCampaign promotionCampaign;
  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_code_id", nullable = false)
  private PromotionCode promotionCode;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_campaign_sightevent_id")
  private PromotionCampaignSightEvent promotionCampaignSightEvent;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sightevent_id")
  private SightEvent sightEvent;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orderentry_id")
  private OrderEntry orderEntry;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  @NotNull
  @Column(nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionCodeRedemptionStatus status = PromotionCodeRedemptionStatus.RESERVED;
  @Column(name = "reservation_token", unique = true)
  private String reservationToken;
  @NotNull
  @Column(name = "reserved_at", nullable = false)
  private Date reservedAt = new Date();
  @Column(name = "reserved_until")
  private Date reservedUntil;
  @Column(name = "used_at")
  private Date usedAt;
  @Column(name = "released_at")
  private Date releasedAt;
  @NotNull
  @Column(name = "code_snapshot", nullable = false)
  private String codeSnapshot;
  @NotNull
  @Column(name = "promotion_name_snapshot", nullable = false)
  private String promotionNameSnapshot;
  @NotNull
  @Column(name = "promotion_type_snapshot", nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionType promotionTypeSnapshot;
  @Column(name = "discount_percent")
  private BigDecimal discountPercent;
  @Column(name = "discount_amount_gross")
  private Integer discountAmountGross;
  @Column(name = "hpt_ticket_definition_id")
  private Long hptTicketDefinitionId;
  @Column(name = "hpt_ticket_pool_definition_id")
  private Long hptTicketPoolDefinitionId;
  @NotNull
  @Column(name = "created_at", nullable = false)
  private Date createdAt = new Date();
  @Column(name = "updated_at")
  private Date updatedAt;

  public PromotionCampaign getPromotionCampaign() {
    return promotionCampaign;
  }

  public void setPromotionCampaign(PromotionCampaign promotionCampaign) {
    this.promotionCampaign = promotionCampaign;
  }

  public PromotionCode getPromotionCode() {
    return promotionCode;
  }

  public void setPromotionCode(PromotionCode promotionCode) {
    this.promotionCode = promotionCode;
  }

  public PromotionCampaignSightEvent getPromotionCampaignSightEvent() {
    return promotionCampaignSightEvent;
  }

  public void setPromotionCampaignSightEvent(PromotionCampaignSightEvent promotionCampaignSightEvent) {
    this.promotionCampaignSightEvent = promotionCampaignSightEvent;
  }

  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public OrderEntry getOrderEntry() {
    return orderEntry;
  }

  public void setOrderEntry(OrderEntry orderEntry) {
    this.orderEntry = orderEntry;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public PromotionCodeRedemptionStatus getStatus() {
    return status;
  }

  public void setStatus(PromotionCodeRedemptionStatus status) {
    this.status = status;
  }

  public String getReservationToken() {
    return reservationToken;
  }

  public void setReservationToken(String reservationToken) {
    this.reservationToken = reservationToken;
  }

  public Date getReservedAt() {
    return reservedAt;
  }

  public void setReservedAt(Date reservedAt) {
    this.reservedAt = reservedAt;
  }

  public Date getReservedUntil() {
    return reservedUntil;
  }

  public void setReservedUntil(Date reservedUntil) {
    this.reservedUntil = reservedUntil;
  }

  public Date getUsedAt() {
    return usedAt;
  }

  public void setUsedAt(Date usedAt) {
    this.usedAt = usedAt;
  }

  public Date getReleasedAt() {
    return releasedAt;
  }

  public void setReleasedAt(Date releasedAt) {
    this.releasedAt = releasedAt;
  }

  public String getCodeSnapshot() {
    return codeSnapshot;
  }

  public void setCodeSnapshot(String codeSnapshot) {
    this.codeSnapshot = codeSnapshot;
  }

  public String getPromotionNameSnapshot() {
    return promotionNameSnapshot;
  }

  public void setPromotionNameSnapshot(String promotionNameSnapshot) {
    this.promotionNameSnapshot = promotionNameSnapshot;
  }

  public PromotionType getPromotionTypeSnapshot() {
    return promotionTypeSnapshot;
  }

  public void setPromotionTypeSnapshot(PromotionType promotionTypeSnapshot) {
    this.promotionTypeSnapshot = promotionTypeSnapshot;
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

  public Long getHptTicketDefinitionId() {
    return hptTicketDefinitionId;
  }

  public void setHptTicketDefinitionId(Long hptTicketDefinitionId) {
    this.hptTicketDefinitionId = hptTicketDefinitionId;
  }

  public Long getHptTicketPoolDefinitionId() {
    return hptTicketPoolDefinitionId;
  }

  public void setHptTicketPoolDefinitionId(Long hptTicketPoolDefinitionId) {
    this.hptTicketPoolDefinitionId = hptTicketPoolDefinitionId;
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
}
