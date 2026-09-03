package pl.hellopoland.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExceptionMessagesServiceTest {

  private final ExceptionMessagesService service = new ExceptionMessagesService();

  @Test
  public void shouldReadPartnerMessageByKey() {
    assertEquals("Podaj adres e-mail partnera.",
        service.getMessageByKey("partner.create.email.required"));
  }

  @Test
  public void shouldFormatPartnerMessage() {
    assertEquals(
        "Nie udało się wysłać danych dostępowych na adres helpdesk@example.com. "
            + "Partner nie został utworzony.",
        service.getFormattedMessageByKey(
            "partner.create.credentials.sendFailed", "helpdesk@example.com"));
  }
}
