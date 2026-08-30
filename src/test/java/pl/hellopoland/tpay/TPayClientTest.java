package pl.hellopoland.tpay;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TPayClientTest {

  @Test
  public void shouldBuildStandardOrderConfirmationUrl() {
    assertEquals(
        "https://hello-poland.pl/zamowienie?id=order-hash",
        TPayClient.buildPayerSuccessUrl("https://hello-poland.pl", "order-hash", false));
  }

  @Test
  public void shouldBuildWidgetOrderConfirmationUrl() {
    assertEquals(
        "https://hello-poland.pl/widget/order/?id=order-hash",
        TPayClient.buildPayerSuccessUrl("https://hello-poland.pl", "order-hash", true));
  }
}
