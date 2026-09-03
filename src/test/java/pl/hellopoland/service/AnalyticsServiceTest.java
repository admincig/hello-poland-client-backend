package pl.hellopoland.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import org.junit.Test;
import pl.hellopoland.bo.Discount;
import pl.hellopoland.bo.OrderEntry;

public class AnalyticsServiceTest {

  private final AnalyticsService service = new AnalyticsService();

  @Test
  public void calculatesCommissionWhenDiscountHasNoHelloPolandPart() {
    OrderEntry orderEntry = new OrderEntry();
    orderEntry.setUnitPrice(10000);
    orderEntry.setQuantity(2);
    orderEntry.setDiscount(new Discount());

    BigDecimal commission = service.calculateCommission(new BigDecimal("10"), orderEntry);

    assertEquals(new BigDecimal("20.00"), commission);
  }
}
