package pl.hellopoland.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.Test;
import pl.hellopoland.bo.PromotionCampaign;

public class PromotionCodeServiceTest {

  private final PromotionCodeService service = new PromotionCodeService();
  private final ZoneId zone = ZoneId.systemDefault();

  @Test
  public void acceptsVisitOnLastConfiguredTicketValidityDay() {
    PromotionCampaign campaign = campaignValidTo(2026, 12, 31, 23, 59);

    assertTrue(service.isVisitDateWithinTicketValidity(
        campaign, date(2026, 12, 31, 0, 0)));
  }

  @Test
  public void rejectsVisitAfterConfiguredTicketValidityDay() {
    PromotionCampaign campaign = campaignValidTo(2026, 12, 31, 23, 59);

    assertFalse(service.isVisitDateWithinTicketValidity(
        campaign, date(2027, 1, 1, 0, 0)));
  }

  @Test
  public void rejectsVisitWhenTicketValidityIsNotConfigured() {
    assertFalse(service.isVisitDateWithinTicketValidity(
        new PromotionCampaign(), date(2026, 12, 31, 0, 0)));
  }

  private PromotionCampaign campaignValidTo(int year, int month, int day, int hour, int minute) {
    PromotionCampaign campaign = new PromotionCampaign();
    campaign.setTicketValidTo(date(year, month, day, hour, minute));
    return campaign;
  }

  private Date date(int year, int month, int day, int hour, int minute) {
    return Date.from(LocalDateTime.of(year, month, day, hour, minute).atZone(zone).toInstant());
  }
}
