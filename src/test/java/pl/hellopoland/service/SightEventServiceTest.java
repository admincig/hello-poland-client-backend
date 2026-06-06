package pl.hellopoland.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import pl.hellopoland.dto.DiscountDTO;
import pl.hellopoland.dto.DiscountTypeDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;

public class SightEventServiceTest {

  private final SightEventService service = new SightEventService();

  @Test
  public void calculatesDiscountPriceFromOriginalPriceAndDiscountAmount() {
    TicketDefinitionDTO ticket = ticketWithDiscountAmount(4400, 500, 104000);

    assertEquals(Integer.valueOf(3900), service.getValidDiscountPrice(ticket));
  }

  @Test
  public void calculatesDiscountPriceFromOriginalPriceAndFlatDiscountValue() {
    TicketDefinitionDTO ticket = ticketWithDiscountValue(4400, DiscountTypeDTO.FLAT, 500);

    assertEquals(Integer.valueOf(3900), service.getValidDiscountPrice(ticket));
  }

  @Test
  public void calculatesDiscountPriceFromOriginalPriceAndPercentDiscountValue() {
    TicketDefinitionDTO ticket = ticketWithDiscountValue(4400, DiscountTypeDTO.PERCENT, 10);

    assertEquals(Integer.valueOf(3960), service.getValidDiscountPrice(ticket));
  }

  @Test
  public void ignoresDiscountEqualToOriginalPrice() {
    TicketDefinitionDTO ticket = ticketWithDiscountAmount(4400, 4400, 0);

    assertNull(service.getValidDiscountPrice(ticket));
  }

  @Test
  public void ignoresDiscountHigherThanOriginalPrice() {
    TicketDefinitionDTO ticket = ticketWithDiscountAmount(4400, 104000, 0);

    assertNull(service.getValidDiscountPrice(ticket));
  }

  private TicketDefinitionDTO ticketWithDiscountAmount(Integer originalPrice, Integer discountAmount,
      Integer legacyDiscountPrice) {
    TicketDefinitionDTO ticket = new TicketDefinitionDTO();
    ticket.originalPrice = originalPrice;
    ticket.discount = new DiscountDTO();
    ticket.discount.amount = discountAmount;
    ticket.discount.price = legacyDiscountPrice;
    return ticket;
  }

  private TicketDefinitionDTO ticketWithDiscountValue(Integer originalPrice, DiscountTypeDTO type,
      Integer discountValue) {
    TicketDefinitionDTO ticket = new TicketDefinitionDTO();
    ticket.originalPrice = originalPrice;
    ticket.discount = new DiscountDTO();
    ticket.discount.type = type;
    ticket.discount.value = discountValue;
    return ticket;
  }
}
