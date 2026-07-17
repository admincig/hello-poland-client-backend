package pl.hellopoland.bo;

import pl.hellopoland.enums.PromotionCampaignSightEventSource;
import pl.hellopoland.enums.PromotionTicketPoolStatus;

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
@Table(name = "promotion_campaign_sightevent")
public class PromotionCampaignSightEvent extends ModelSuperclass {

  private static final long serialVersionUID = 4103251165622746661L;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_campaign_id", nullable = false)
  private PromotionCampaign promotionCampaign;
  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "sightevent_id", nullable = false)
  private SightEvent sightEvent;
  @NotNull
  @Column(nullable = false)
  private boolean active = true;
  @NotNull
  @Column(nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionCampaignSightEventSource source = PromotionCampaignSightEventSource.MANUAL;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "source_tag_id")
  private Tag sourceTag;
  @Column(name = "hpt_sight_event_id")
  private Long hptSightEventId;
  @Column(name = "hpt_atna_id")
  private Long hptAtnaId;
  @Column(name = "hpt_ticket_definition_id")
  private Long hptTicketDefinitionId;
  @Column(name = "hpt_ticket_pool_definition_id")
  private Long hptTicketPoolDefinitionId;
  @NotNull
  @Column(name = "ticket_pool_status", nullable = false, length = 32)
  @Enumerated(EnumType.STRING)
  private PromotionTicketPoolStatus ticketPoolStatus = PromotionTicketPoolStatus.NOT_REQUIRED;
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

  public SightEvent getSightEvent() {
    return sightEvent;
  }

  public void setSightEvent(SightEvent sightEvent) {
    this.sightEvent = sightEvent;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public PromotionCampaignSightEventSource getSource() {
    return source;
  }

  public void setSource(PromotionCampaignSightEventSource source) {
    this.source = source;
  }

  public Tag getSourceTag() {
    return sourceTag;
  }

  public void setSourceTag(Tag sourceTag) {
    this.sourceTag = sourceTag;
  }

  public Long getHptSightEventId() {
    return hptSightEventId;
  }

  public void setHptSightEventId(Long hptSightEventId) {
    this.hptSightEventId = hptSightEventId;
  }

  public Long getHptAtnaId() {
    return hptAtnaId;
  }

  public void setHptAtnaId(Long hptAtnaId) {
    this.hptAtnaId = hptAtnaId;
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

  public PromotionTicketPoolStatus getTicketPoolStatus() {
    return ticketPoolStatus;
  }

  public void setTicketPoolStatus(PromotionTicketPoolStatus ticketPoolStatus) {
    this.ticketPoolStatus = ticketPoolStatus;
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
