package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionCampaignSightEventSource;
import pl.hellopoland.enums.PromotionTicketPoolStatus;

import java.util.Date;

public class PromotionCampaignSightEventHelpdeskDTO {

  public Long id;
  public Long promotionCampaignId;
  public Long sightEventId;
  public String sightEventName;
  public Long sourceTagId;
  public Boolean active;
  public PromotionCampaignSightEventSource source;
  public Long hptSightEventId;
  public Long hptAtnaId;
  public Long hptTicketDefinitionId;
  public Long hptTicketPoolDefinitionId;
  public PromotionTicketPoolStatus ticketPoolStatus;
  public Date createdAt;
  public Date updatedAt;
}
