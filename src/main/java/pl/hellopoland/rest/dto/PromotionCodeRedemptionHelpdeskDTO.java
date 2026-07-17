package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionCodeRedemptionStatus;
import pl.hellopoland.enums.PromotionType;

import java.math.BigDecimal;
import java.util.Date;

public class PromotionCodeRedemptionHelpdeskDTO {

  public Long id;
  public Long promotionCampaignId;
  public Long promotionCodeId;
  public Long promotionCampaignSightEventId;
  public Long sightEventId;
  public Long orderId;
  public Long orderEntryId;
  public Long userId;
  public PromotionCodeRedemptionStatus status;
  public String reservationToken;
  public Date reservedAt;
  public Date reservedUntil;
  public Date usedAt;
  public Date releasedAt;
  public String codeSnapshot;
  public String promotionNameSnapshot;
  public PromotionType promotionTypeSnapshot;
  public BigDecimal discountPercent;
  public Integer discountAmountGross;
  public Long hptTicketDefinitionId;
  public Long hptTicketPoolDefinitionId;
  public Date createdAt;
  public Date updatedAt;
}
