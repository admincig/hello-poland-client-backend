package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionCodeStatus;
import pl.hellopoland.enums.PromotionCodeType;

import java.util.Date;

public class PromotionCodeHelpdeskDTO {

  public Long id;
  public Long promotionCampaignId;
  public Long promotionCodeBatchId;
  public String code;
  public PromotionCodeType codeType;
  public PromotionCodeStatus status;
  public Integer maxRedemptions;
  public Integer maxRedemptionsPerCustomer;
  public Integer maxRedemptionsPerDay;
  public Integer reservedRedemptionsCount;
  public Integer usedRedemptionsCount;
  public Date reservedUntil;
  public Date createdAt;
  public Date updatedAt;
  public Date disabledAt;
}
