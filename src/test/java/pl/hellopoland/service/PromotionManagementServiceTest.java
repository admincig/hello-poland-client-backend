package pl.hellopoland.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.Test;
import pl.hellopoland.bo.PromotionCampaign;
import pl.hellopoland.bo.PromotionCampaignSightEvent;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.dto.FrequencyTypeDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PromotionCodeSetupIRO;
import pl.hellopoland.rest.dto.PromotionTicketPoolSetupIRO;

public class PromotionManagementServiceTest {

  private final PromotionManagementService service = new PromotionManagementService();
  private final ZoneId zone = ZoneId.systemDefault();

  @Test
  public void createsWholeDayDailyPoolThroughConfiguredTicketValidityDate() {
    PromotionCampaign campaign = new PromotionCampaign();
    campaign.setName("Test Promotion");
    campaign.setValidFrom(date(2026, 8, 21, 15, 15));
    campaign.setValidTo(date(2026, 8, 30, 16, 45));
    campaign.setTicketValidTo(date(2026, 12, 31, 23, 59));

    SightEvent sightEvent = new SightEvent();
    sightEvent.setHptId(123L);
    PromotionCampaignSightEvent relation = new PromotionCampaignSightEvent();
    relation.setSightEvent(sightEvent);

    PromotionTicketPoolSetupIRO setup = new PromotionTicketPoolSetupIRO();
    setup.availableTicketsNumber = 100;

    TicketDefinitionDTO ticketDefinition = new TicketDefinitionDTO();
    ticketDefinition.id = 456L;

    TicketPoolDefinitionDTO pool = service.buildSpecialTicketPoolDefinition(
        campaign, relation, setup, ticketDefinition);

    assertLocalDateTime(LocalDateTime.of(2026, 8, 21, 0, 0), pool.startDate);
    assertLocalDateTime(LocalDateTime.of(2026, 8, 21, 23, 59, 59, 999_000_000), pool.endDate);
    assertLocalDateTime(LocalDateTime.of(2026, 8, 21, 0, 0), pool.entryStartDate);
    assertLocalDateTime(LocalDateTime.of(2026, 8, 21, 23, 59, 59, 999_000_000),
        pool.entryEndDate);
    assertLocalDateTime(LocalDateTime.of(2026, 8, 21, 0, 0),
        pool.frequencyData.startDate);
    assertLocalDateTime(LocalDateTime.of(2026, 12, 31, 23, 59, 59, 999_000_000),
        pool.frequencyData.endDate);
    assertEquals(Long.valueOf(123L), pool.sightEventId);
    assertEquals(Long.valueOf(456L), pool.ticketDefinitions.get(0).id);
    assertTrue(pool.wholeDay);
    assertTrue(pool.isCyclic);
    assertEquals(FrequencyTypeDTO.DAILY, pool.frequencyData.frequencyType);
    assertFalse(pool.visibleForPartner);
    assertFalse(pool.visibleOnPortal);
  }

  @Test
  public void respectsExplicitPromotionalTicketValidityEndDate() {
    PromotionCampaign campaign = new PromotionCampaign();
    campaign.setName("Test Promotion");
    campaign.setValidFrom(date(2026, 8, 25, 0, 0));
    campaign.setValidTo(date(2026, 9, 30, 23, 59));
    campaign.setTicketValidTo(date(2026, 12, 31, 23, 59));

    SightEvent sightEvent = new SightEvent();
    sightEvent.setHptId(123L);
    PromotionCampaignSightEvent relation = new PromotionCampaignSightEvent();
    relation.setSightEvent(sightEvent);

    PromotionTicketPoolSetupIRO setup = new PromotionTicketPoolSetupIRO();
    setup.availableTicketsNumber = 100;
    setup.endDate = date(2026, 10, 15, 12, 0);

    TicketDefinitionDTO ticketDefinition = new TicketDefinitionDTO();
    ticketDefinition.id = 456L;

    TicketPoolDefinitionDTO pool = service.buildSpecialTicketPoolDefinition(
        campaign, relation, setup, ticketDefinition);

    assertLocalDateTime(LocalDateTime.of(2026, 10, 15, 23, 59, 59, 999_000_000),
        pool.frequencyData.endDate);
  }

  @Test(expected = ConflictingException.class)
  public void rejectsPoolValidityAfterCampaignTicketBoundary() {
    PromotionCampaign campaign = new PromotionCampaign();
    campaign.setName("Test Promotion");
    campaign.setValidFrom(date(2026, 8, 25, 0, 0));
    campaign.setValidTo(date(2026, 9, 30, 23, 59));
    campaign.setTicketValidTo(date(2026, 12, 31, 23, 59));

    SightEvent sightEvent = new SightEvent();
    sightEvent.setHptId(123L);
    PromotionCampaignSightEvent relation = new PromotionCampaignSightEvent();
    relation.setSightEvent(sightEvent);

    PromotionTicketPoolSetupIRO setup = new PromotionTicketPoolSetupIRO();
    setup.availableTicketsNumber = 100;
    setup.endDate = date(2027, 1, 1, 0, 0);

    TicketDefinitionDTO ticketDefinition = new TicketDefinitionDTO();
    ticketDefinition.id = 456L;

    service.buildSpecialTicketPoolDefinition(campaign, relation, setup, ticketDefinition);
  }

  @Test(expected = ConflictingException.class)
  public void rejectsFreePromotionalTicket() {
    PromotionTicketPoolSetupIRO setup = new PromotionTicketPoolSetupIRO();
    setup.ticketPrice = 0;
    setup.availableTicketsNumber = 100;

    service.validateTicketPoolSetup(setup);
  }

  @Test
  public void acceptsOnePlnPromotionalTicket() {
    PromotionTicketPoolSetupIRO setup = new PromotionTicketPoolSetupIRO();
    setup.ticketPrice = 100;
    setup.availableTicketsNumber = 100;

    service.validateTicketPoolSetup(setup);
  }

  @Test
  public void usesGeneratedCodesCountReceivedFromForm() {
    PromotionCodeSetupIRO setup = new PromotionCodeSetupIRO();
    setup.generateCount = 237;

    assertEquals(237, service.requestedGeneratedCodesCount(setup));
  }

  @Test(expected = ConflictingException.class)
  public void rejectsGeneratedCodesCountAboveLimit() {
    PromotionCodeSetupIRO setup = new PromotionCodeSetupIRO();
    setup.generateCount = 100001;

    service.requestedGeneratedCodesCount(setup);
  }

  private Date date(int year, int month, int day, int hour, int minute) {
    return Date.from(LocalDateTime.of(year, month, day, hour, minute).atZone(zone).toInstant());
  }

  private void assertLocalDateTime(LocalDateTime expected, Date actual) {
    assertEquals(expected, LocalDateTime.ofInstant(actual.toInstant(), zone));
  }
}
