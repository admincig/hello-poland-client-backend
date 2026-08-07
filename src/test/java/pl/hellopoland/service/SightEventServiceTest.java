package pl.hellopoland.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import pl.hellopoland.dto.DiscountDTO;
import pl.hellopoland.dto.DiscountTypeDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.dto.TicketPoolTypeDTO;
import pl.hellopoland.service.vo.HptTpdsDownloadConfigurator;

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

  @Test
  public void helpdeskShowsPartnerPoolsAndAdditionalPromotionalPools() {
    TicketPoolDefinitionDTO partnerPool = pool(TicketPoolTypeDTO.STANDARD, true, false);
    TicketPoolDefinitionDTO promotionalPool = pool(TicketPoolTypeDTO.PROMOTIONAL, false, false);
    TicketPoolDefinitionDTO hiddenStandardPool = pool(TicketPoolTypeDTO.STANDARD, false, false);
    TicketPoolDefinitionDTO deletedPool = pool(TicketPoolTypeDTO.STANDARD, true, true);

    List<TicketPoolDefinitionDTO> result = service.filterTicketPoolDefinitionsByAudience(
        Arrays.asList(partnerPool, promotionalPool, hiddenStandardPool, deletedPool),
        HptTpdsDownloadConfigurator.Audience.HELPDESK);

    assertEquals(Arrays.asList(partnerPool, promotionalPool), result);
  }

  @Test
  public void partnerDoesNotShowPromotionalOrHiddenPools() {
    TicketPoolDefinitionDTO partnerPool = pool(TicketPoolTypeDTO.STANDARD, true, false);
    TicketPoolDefinitionDTO promotionalPool = pool(TicketPoolTypeDTO.PROMOTIONAL, false, false);
    TicketPoolDefinitionDTO hiddenStandardPool = pool(TicketPoolTypeDTO.STANDARD, false, false);

    List<TicketPoolDefinitionDTO> result = service.filterTicketPoolDefinitionsByAudience(
        Arrays.asList(partnerPool, promotionalPool, hiddenStandardPool),
        HptTpdsDownloadConfigurator.Audience.PARTNER);

    assertEquals(List.of(partnerPool), result);
  }

  @Test
  public void canonicalizesValidVoivodeshipNames() {
    assertEquals("Mazowieckie", service.canonicalVoivodeship(" mazowieckie "));
    assertEquals("Kujawsko-pomorskie", service.canonicalVoivodeship("KUJAWSKO-POMORSKIE"));
  }

  @Test
  public void rejectsInvalidVoivodeshipNames() {
    assertNull(service.canonicalVoivodeship(null));
    assertNull(service.canonicalVoivodeship("kujawsko-toruńskie"));
  }

  private TicketPoolDefinitionDTO pool(TicketPoolTypeDTO poolType, boolean visibleForPartner,
      boolean deleted) {
    TicketPoolDefinitionDTO pool = new TicketPoolDefinitionDTO();
    pool.poolType = poolType;
    pool.visibleForPartner = visibleForPartner;
    pool.deleted = deleted;
    return pool;
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
