package pl.hellopoland.rest.dto;

import pl.hellopoland.enums.PromotionTicketPoolStatus;

public class PromotionTicketPoolTargetPreviewDTO {

  public Long sightEventId;
  public String sightEventName;
  public Long hptSightEventId;
  public Long sightId;
  public String sightName;
  public Long partnerId;
  public String partnerName;
  public Long relationId;
  public Boolean alreadyInCampaign;
  public Boolean activeInCampaign;
  public Long hptAtnaId;
  public Long hptTicketDefinitionId;
  public Long hptTicketPoolDefinitionId;
  public PromotionTicketPoolStatus ticketPoolStatus;
}
