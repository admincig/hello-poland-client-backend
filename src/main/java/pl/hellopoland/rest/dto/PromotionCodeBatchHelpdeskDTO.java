package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionCodeBatchSource;

import java.util.Date;

public class PromotionCodeBatchHelpdeskDTO {

  public Long id;
  public Long promotionCampaignId;
  public PromotionCodeBatchSource source;
  public String fileName;
  public Integer codesCount;
  public Date createdAt;
}
