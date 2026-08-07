package pl.hellopoland.service.api.market;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.dto.TicketPoolTypeDTO;
import pl.hellopoland.service.SightEventService;

public class SearchServiceMarketAPITest {

  private final SearchServiceMarketAPI service = new SearchServiceMarketAPI();

  @Before
  public void setUp() {
    service.seService = new SightEventService();
  }

  @Test
  public void returnsCheapestSightEventWithItsOwnDiscountPrice() {
    SightEventDTO trampolinePark = sightEvent(4400, null);
    SightEventDTO summerCamp = sightEvent(111900, 104000);

    SightEventDTO cheapest =
        service.findCheapestPricedSightEvent(List.of(trampolinePark, summerCamp)).orElse(null);

    assertEquals(Integer.valueOf(4400), cheapest.minPrice);
    assertNull(cheapest.minDiscountPrice);
  }

  @Test
  public void doesNotMixDiscountPriceFromCheaperDiscountedSightEvent() {
    SightEventDTO standard = sightEvent(4400, null);
    SightEventDTO discounted = sightEvent(5800, 3900);

    SightEventDTO cheapest =
        service.findCheapestPricedSightEvent(List.of(standard, discounted)).orElse(null);

    assertEquals(Integer.valueOf(4400), cheapest.minPrice);
    assertNull(cheapest.minDiscountPrice);
  }

  @Test
  public void keepsDiscountPriceWhenItBelongsToCheapestSightEvent() {
    SightEventDTO discounted = sightEvent(4400, 3900);
    SightEventDTO standard = sightEvent(5800, null);

    SightEventDTO cheapest =
        service.findCheapestPricedSightEvent(List.of(discounted, standard)).orElse(null);

    assertEquals(Integer.valueOf(4400), cheapest.minPrice);
    assertEquals(Integer.valueOf(3900), cheapest.minDiscountPrice);
  }

  @Test
  public void excludesSightEventsWithoutAnyPoolFromPortalSearch() {
    SightEventDTO sightEvent = new SightEventDTO();
    sightEvent.ticketPoolDefinitions = List.of();

    assertEquals(List.of(), service.filterAvailableOnPortal(List.of(sightEvent)));
  }

  @Test
  public void excludesSightEventsWithOnlyPromotionalPoolFromPortalSearch() {
    SightEventDTO sightEvent = new SightEventDTO();
    sightEvent.ticketPoolDefinitions = List.of(pool(TicketPoolTypeDTO.PROMOTIONAL, false));

    assertEquals(List.of(), service.filterAvailableOnPortal(List.of(sightEvent)));
  }

  @Test
  public void keepsSightEventsWithPortalVisibleStandardPoolInSearch() {
    SightEventDTO sightEvent = new SightEventDTO();
    sightEvent.ticketPoolDefinitions = List.of(pool(TicketPoolTypeDTO.STANDARD, true));

    assertEquals(List.of(sightEvent), service.filterAvailableOnPortal(List.of(sightEvent)));
  }

  private TicketPoolDefinitionDTO pool(TicketPoolTypeDTO poolType, boolean visibleOnPortal) {
    TicketPoolDefinitionDTO pool = new TicketPoolDefinitionDTO();
    pool.poolType = poolType;
    pool.visibleOnPortal = visibleOnPortal;
    pool.isCyclic = true;
    pool.startDate = new Date(System.currentTimeMillis() + 60_000L);
    return pool;
  }

  private SightEventDTO sightEvent(Integer minPrice, Integer minDiscountPrice) {
    SightEventDTO sightEvent = new SightEventDTO();
    sightEvent.minPrice = minPrice;
    sightEvent.minDiscountPrice = minDiscountPrice;
    return sightEvent;
  }
}
