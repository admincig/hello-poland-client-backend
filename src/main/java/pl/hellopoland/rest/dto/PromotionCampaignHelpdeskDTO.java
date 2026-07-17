package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionScopeType;
import pl.hellopoland.enums.PromotionStatus;
import pl.hellopoland.enums.PromotionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromotionCampaignHelpdeskDTO {

  public Long id;
  public String name;
  public PromotionType promotionType;
  public PromotionStatus status;
  public PromotionScopeType scopeType;
  public Date validFrom;
  public Date validTo;
  public Integer globalLimit;
  public Integer codeLimit;
  public Integer customerLimit;
  public Integer dailyLimit;
  public Integer requiredTicketQuantity;
  public Integer grantedTicketQuantity;
  public Integer reservedRedemptionsCount;
  public Integer usedRedemptionsCount;
  public BigDecimal discountPercent;
  public Integer discountAmountGross;
  public Date createdAt;
  public Date updatedAt;
  public List<Long> tagIds = new ArrayList<>();
  public List<PromotionCampaignSightEventHelpdeskDTO> sightEvents = new ArrayList<>();
  public PromotionCodeSetupIRO codeSetup;
  public PromotionTargetSetupIRO targetSetup;
  public PromotionTicketPoolSetupIRO ticketPoolSetup;
}
