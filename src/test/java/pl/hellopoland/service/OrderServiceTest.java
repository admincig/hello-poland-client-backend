package pl.hellopoland.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OrderServiceTest {

  private final OrderService service = new OrderService();

  @Test
  public void explainsUnavailablePromotionalTicketDate() {
    String message = service.orderReservationErrorMessage(
        new RuntimeException("Ządana data poza zakresem definicji puli"));

    assertEquals("Wybrany termin nie jest dostępny dla jednego z biletów. "
        + "Wróć do koszyka, wybierz inny termin i spróbuj ponownie.", message);
  }

  @Test
  public void explainsSoldOutTickets() {
    String message = service.orderReservationErrorMessage(
        new RuntimeException("Brak dostępnych biletów na wybrane wydarzenie."));

    assertEquals("Bilety na wybrany termin zostały wyprzedane. "
        + "Wróć do koszyka i wybierz inny termin lub ofertę.", message);
  }

  @Test
  public void replacesUnknownExternalErrorWithActionableMessage() {
    String message = service.orderReservationErrorMessage(
        new RuntimeException("Nieznany błąd."));

    assertEquals("Nie udało się zarezerwować biletów w wybranym terminie. "
        + "Sprawdź dostępność terminu i spróbuj ponownie.", message);
  }

  @Test
  public void preservesSpecificExternalError() {
    String message = service.orderReservationErrorMessage(
        new RuntimeException("Rezerwacja wygasła."));

    assertEquals("Rezerwacja wygasła.", message);
  }
}
