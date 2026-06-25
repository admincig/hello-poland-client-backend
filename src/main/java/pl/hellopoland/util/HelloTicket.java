package pl.hellopoland.util;

import org.apache.commons.io.IOUtils;
import pl.hellopoland.bo.*;
import pl.hellopoland.dto.*;
import pl.hellopoland.dto.booking.BookingDTO;
import pl.hellopoland.dto.booking.TicketDTO;
import pl.hellopoland.dto.booking.TicketOrderDTO;
import pl.hellopoland.exception.badrequest.BadRequestException;
import pl.hellopoland.exception.conflict.CannotDeleteSightEventFromExternalSystemException;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.conflict.ExternalSystemException;
import pl.hellopoland.exception.email.EmailSendingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.rest.JsonbConfig;

import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonStructure;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.System.Logger.Level;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static jakarta.ws.rs.core.Response.Status.OK;
import static java.lang.System.Logger.Level.WARNING;
import static java.util.stream.Collectors.joining;

public class HelloTicket {

  public HelloTicket(String url) {
    this.url = url;
  }

  private System.Logger logger = System.getLogger(HelloTicket.class.getName());
  private final pl.hellopoland.exception.ExceptionMessagesService exceptionMessagesService =
      new pl.hellopoland.exception.ExceptionMessagesService();

  private String url;
  private static final int CONNECT_TIMEOUT_MS = 5000;
  private static final int READ_TIMEOUT_MS = 15000;
  private static final String GENERIC_EXTERNAL_SAVE_ERROR =
      "Nie udało się zapisać danych w zewnętrznym systemie. Sprawdź poprawność danych i spróbuj ponownie.";
  private static final String EXTERNAL_TEXT_TOO_LONG_ERROR =
      "Jedno z pól tekstowych jest za długie. Skróć opis lub wskazówki dojazdu i spróbuj ponownie.";

  private static final String AUTH_TOKEN =
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJIZWxsbyBQb2xhbmQiLCJhdXRoIjoiUk9MRV9FWFRFUk5BTF9VU0VSIn0.AODtF8AEqe-egeWKn2zPhfo2hWplkSbfFfFrNH6mpfsV9McC89paYns3sR_5LPx_V4pxpPOtgTMK7A0pCsJ3mA";

  public JsonStructure book(OrderDetails details, List<OrderEntry> orderEntries) {
    BookingDTO booking = new BookingDTO();
    booking.buyerNotes = details.getBuyerNotes();
    booking.customerEmail = details.getEmail();
    booking.customerName = details.getFirstName() + " " + details.getLastName();
    booking.invoice = details.getInvoice();
    List<TicketOrderDTO> ticketBookings = orderEntries.stream().map(oe -> {
      TicketOrderDTO t = new TicketOrderDTO();
      t.ticketDefinitionId = oe.getExternalDefinitionId();
      t.ticketPoolDefinitionId = oe.getPoolId();
      t.numberOfTickets = oe.getQuantity().longValue();
      t.date = oe.getDateEntry().getDate();
      return t;
    }).collect(Collectors.toList());
    booking.ticketBookings = ticketBookings;
    var json = JsonbConfig.getInstance().toJson(booking);
    try {
      var resp = post("/v1/bookings", json, AUTH_TOKEN);
      booking = JsonbConfig.getInstance().fromJson(resp.toString(), BookingDTO.class);
      for (var oe : orderEntries) {
        var ose = oe.getDateEntry().getSightEntry();
        ose.setSerialNumber(booking.serialNumber);
        for (var iter = booking.tickets.iterator(); iter.hasNext();) {
          TicketDTO ticket = iter.next();
          if (oe.matches(ticket)) {
            oe.setExternalId((long) ticket.id);
            oe.getDateEntry().setDate(ticket.date);
            ose.setWholeDay(ticket.wholeDay);
            break;
          }
        }
      }
      return resp;
    } catch (IOException e) {
      logger.log(WARNING, e);
      return null;
    }
  }

  public JsonStructure confirm(String serialNumber, List<OrderEntry> orderEntries) {
    try {
      var tPayPaymentId = Optional.ofNullable(orderEntries.get(0)).map(OrderEntry::getDateEntry)
          .map(OrderDateEntry::getSightEntry).map(OrderSightEntry::getOrder)
          .map(Order::getTPayPaymentId).orElse("-----");
      var resp = put("/v1/bookings/buy/" + serialNumber + "/" + tPayPaymentId + "/PLN",
          null, AUTH_TOKEN);
      BookingDTO booking = JsonbConfig.getInstance().fromJson(resp.toString(), BookingDTO.class);
      for (var oe : orderEntries) {
        for (var iter = booking.tickets.iterator(); iter.hasNext();) {
          TicketDTO ticket = iter.next();
          if (oe.matches(ticket)) {
            oe.addNumber(ticket.serialNumber);
          }
        }
      }
      return resp;
    } catch (IOException e) {
      logger.log(WARNING, e);
      return null;
    }
  }

  public SightEventDTO addSightEvent(SightEventDTO dto, String partnerAuthToken) {
    String json = JsonbConfig.getInstance().toJson(dto);

    try {
      return JsonbConfig.getInstance().fromJson(
          post("/v1/sight-events", json, partnerAuthToken).toString(), SightEventDTO.class);
    } catch (Exception e) {
      logger.log(Level.ERROR, e);
      throw mapExternalException(e);
    }
  }

  public TicketDefinitionDTO addTicketDefinition(TicketDefinitionDTO dto, String partnerAuthToken) {
    String json = JsonbConfig.getInstance().toJson(dto);

    try {
      return JsonbConfig.getInstance().fromJson(
          post("/v1/ticket-definitions", json, partnerAuthToken).toString(),
          TicketDefinitionDTO.class);
    } catch (Exception e) {
      logger.log(Level.ERROR, e);
      throw mapExternalException(e);
    }
  }

  public void deleteSightEvent(SightEvent sightEvent, String partnerAuthToken) {
    try {
      delete("/v1/sight-events/" + sightEvent.getHptId(), partnerAuthToken);
    } catch (IOException e) {
      throw new CannotDeleteSightEventFromExternalSystemException();
    }
  }

  public SightEventDTO updateSightEvent(SightEventDTO sightEvent, String partnerAuthToken) {
    String sightEventJson = JsonbConfig.getInstance().toJson(sightEvent);

    try {
      return JsonbConfig.getInstance().fromJson(
          put("/v1/sight-events/" + sightEvent.id, sightEventJson, partnerAuthToken).toString(),
          SightEventDTO.class);
    } catch (Exception e) {
      logger.log(Level.ERROR, e);
      throw mapExternalException(e);
    }
  }

  public List<TicketPoolDefinitionDTO> getTicketPoolDefinitions(String authToken,
      List<Long> sightEventIds) {
    try {
      String url = "/v1/ticket-pool-definitions";
      if (sightEventIds != null) {
        url += "?sightEventIds=";
        url += sightEventIds.stream().map(Objects::toString)
            .collect(joining("&sightEventIds="));
      }
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = get(url, authToken);
      JsonArray jsonArray = (JsonArray) json;
      List<TicketPoolDefinitionDTO> dtos = new ArrayList<>();
      jsonArray.forEach(p -> {
        var dto = jsonb.fromJson(p.toString(), TicketPoolDefinitionDTO.class);
        dtos.add(dto);
      });
      return dtos;
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return Collections.emptyList();
    }
  }

  public TicketPoolDefinitionDTO addTicketPoolDefinition(TicketPoolDefinitionDTO dto,
      String partnerAuthToken) {
    try {
      Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = post("/v1/ticket-pool-definitions", jsonb.toJson(dto), partnerAuthToken);
      return jsonb.fromJson(json.toString(), TicketPoolDefinitionDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw mapExternalException(e);
    }
  }

  public TicketPoolDefinitionDTO getTicketPoolDefinition(String hptToken, Long id) {
    try {
      return JsonbConfig.getInstance().fromJson(
          get("/v1/ticket-pool-definitions/" + id, hptToken).toString(),
          TicketPoolDefinitionDTO.class);
    } catch (JsonbException | IOException e) {
      logger.log(WARNING, "Failed", e);
      return null;
    }
  }

  public void deleteTicketPoolDefinition(String hptToken, Long id) {
    try {
      delete("/v1/ticket-pool-definitions/" + id, hptToken);
    } catch (Exception e) {
      throw new ConflictingException(
          "Cannot delete TicketPoolDefinition [id=" + id + "] from external system.");
    }
  }

  public void deleteTicketDefinition(String hptToken, Long id) {
    try {
      delete("/v1/ticket-definitions/" + id, hptToken);
    } catch (Exception e) {
      throw new ConflictingException(
          "Błąd usuwania TicketDefintion: " + e.getMessage(), e);
    }
  }

  public List<TicketDefinitionDTO> getTicketDefinitions(String partnerAuthToken) {
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = get("/v1/ticket-definitions", partnerAuthToken);
      JsonArray jsonArray = (JsonArray) json;
      List<TicketDefinitionDTO> dtos = new ArrayList<>();
      jsonArray.forEach(p -> {
        var dto = jsonb.fromJson(p.toString(), TicketDefinitionDTO.class);
        dtos.add(dto);
      });
      return dtos;
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return null;
    }
  }

  public TicketDefinitionDTO getTicketDefinition(Long id, String partnerAuthToken) {
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = get("/v1/ticket-definitions/" + id, partnerAuthToken);
      return jsonb.fromJson(json.toString(), TicketDefinitionDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return null;
    }
  }

  public List<TicketTypeDTO> getTicketTypes(String authToken) {
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = get("/v1/ticket-types", authToken);
      JsonArray jsonArray = (JsonArray) json;
      List<TicketTypeDTO> dtos = new ArrayList<>();
      jsonArray.forEach(p -> dtos.add(jsonb.fromJson(p.toString(), TicketTypeDTO.class)));
      return dtos;
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return Collections.emptyList();
    }
  }

  public AvailableTicketNumberAssociationDTO checkAvailabilityOfTicketsForSightEvent(
      SightEvent sightEvent, Date fromDate, Date toDate) {
    if (fromDate == null) {
      fromDate = new Date();
    }
    try {
      var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      return JsonbConfig.getInstance()
          .fromJson(get("/v1/available-ticket-number-associations/?sightEventId="
              + sightEvent.getHptId() + "&fromDate=" + dateFormat.format(fromDate)
              + (toDate != null ? ("&toDate=" + dateFormat.format(toDate)) : ""), AUTH_TOKEN)
                  .toString(),
              AvailableTicketNumberAssociationDTO.class);
    } catch (JsonbException | IOException e) {
      logger.log(WARNING, "Failed", e);
      throw new ConflictingException(e.getLocalizedMessage());
    }
  }

  public PartnerDTO addPartner(PartnerDTO dto, String hptToken) {
    try {
      Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = post("/v1/helpdesk/partners", jsonb.toJson(dto), hptToken);
      return jsonb.fromJson(json.toString(), PartnerDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw mapExternalException(e);
    }
  }

  public void removePartner(String partnerEmail, String hptToken) {
    try {
      delete("/v1/helpdesk/partners/" + partnerEmail, hptToken);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
    }
  }

  public void stopSale(String hptToken, Long sightEventHptId, Long ticketPoolDefId, Date date) {
    try {
      final var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String dateString = dateFormat.format(date);
      delete("/v1/sight-events/" + sightEventHptId + "/sale?tpdId=" + ticketPoolDefId + "&date="
          + dateString, hptToken);
    } catch (Exception e) {
      logger
          .log(WARNING,
              "Failed: cannot find sight event for id=" + sightEventHptId + ", ticketPoolDefId="
                  + ticketPoolDefId + " and date=" + SimpleDateFormat.getInstance().format(date),
              e);
      throw new ConflictingException("Brak wydarzenia w danym dniu");
    }
  }

  public void changePartnerCredentials(UserAuthDTO userAuthDTO, String hptToken) {
    try {
      String json = JsonbConfig.getInstance().toJson(userAuthDTO);
      put("/v1/users/me/password", json, hptToken);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw new ConflictingException("Zmiana hasła w zewnętrznym systemie nie powiodła się");
    }
  }

  public void changeUsherPassword(long usherId, UserAuthDTO userAuthDTO, String hptToken) {
    try {
      String json = JsonbConfig.getInstance().toJson(userAuthDTO);
      put("/v1/users/" + usherId + "/password", json, hptToken);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw new ConflictingException("Zmiana hasła w zewnętrznym systemie nie powiodła się");
    }
  }

  public List<UserDTO> getUshersForPartner(String partnerAuthToken) {
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = get("/v1/partners/ushers", partnerAuthToken);
      JsonArray jsonArray = (JsonArray) json;
      List<UserDTO> dtos = new ArrayList<>();
      jsonArray.forEach(p -> {
        var dto = jsonb.fromJson(p.toString(), UserDTO.class);
        dtos.add(dto);
      });
      return dtos;
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return null;
    }
  }

  public UserDTO getUsherForPartner(long usherId, String partnerAuthToken) {
    try {
      return JsonbConfig.getInstance().fromJson(
          get("/v1/partners/ushers/" + usherId, partnerAuthToken).toString(), UserDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw new ResourceNotFoundException();
    }
  }

  public UserDTO updateUsherForPartner(UserDTO usher, String partnerAuthToken) {
    String usherJson = JsonbConfig.getInstance().toJson(usher);
    try {
      return JsonbConfig.getInstance().fromJson(
          put("/v1/partners/ushers/" + usher.id, usherJson, partnerAuthToken).toString(),
          UserDTO.class);
    } catch (IOException e) {
      logger.log(Level.ERROR, e);
      throw new ResourceNotFoundException();
    }
  }

  public EmailSendingReportDTO sendTicketsCopyByPartner(String serialNumber,
      String partnerAuthToken) {
    try {
      return JsonbConfig.getInstance().fromJson(
          get("/v1/partners/bookings/" + serialNumber + "/sendTicketCopy", partnerAuthToken)
              .toString(),
          EmailSendingReportDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw new EmailSendingException();
    }
  }

  public EmailSendingReportDTO sendTicketsCopyByAdmin(String serialNumber, String hptToken) {
    try {
      return JsonbConfig.getInstance().fromJson(
          get("/v1/helpdesk/bookings/" + serialNumber + "/sendTicketCopy", hptToken).toString(),
          EmailSendingReportDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw new EmailSendingException();
    }
  }

  public EmailSendingReportDTO sendTicketsCopyByAdmin(String serialNumber, String recipientEmail,
      String hptToken) {
    try {
      Map<String, String> request = Collections.singletonMap("email", recipientEmail);
      String requestJson = JsonbConfig.getInstance().toJson(request);
      return JsonbConfig.getInstance().fromJson(
          post("/v1/helpdesk/bookings/" + serialNumber + "/sendTicketCopyToEmail", requestJson,
              hptToken).toString(),
          EmailSendingReportDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw new EmailSendingException();
    }
  }

  public SightEventDTO addPdfToSightEvent(Long sightEventHptId, FileDescriptorDTO pdfDto,
      String partnerAuthToken) {
    String pdfJsonString = JsonbConfig.getInstance().toJson(pdfDto);
    try {
      return JsonbConfig.getInstance().fromJson(
          put("/v1/sight-events/" + sightEventHptId + "/pdf", pdfJsonString, partnerAuthToken)
              .toString(),
          SightEventDTO.class);
    } catch (IOException e) {
      logger.log(WARNING, "Failed", e);
      throw new ConflictingException(
          "Wystąpił problem podczas zapisu pdf'a w zewnętrznym systemie.");
    }
  }

  public void deletePdfFromSightEvent(SightEvent sightEvent, String partnerAuthToken) {
    String pdfPath = sightEvent.getPdfAttachment().getPath();
    var pdfName = pdfPath.substring(pdfPath.lastIndexOf(File.separator) + 1);
    try {
      delete("/v1/sight-events/" + sightEvent.getHptId() + "/pdf/" + pdfName, partnerAuthToken);
    } catch (IOException e) {
      logger.log(WARNING, "Failed", e);
      throw new ConflictingException(
          "Wystąpił problem podczas usówania pdf'a w zewnętrznym systemie.");
    }
  }

  public UserDTO createUsherForLoggedPartner(UserDTO usherDTO, String partnerAuthToken) {
    String jsonString = JsonbConfig.getInstance().toJson(usherDTO);
    try {
      return JsonbConfig.getInstance().fromJson(
          post("/v1/partners/ushers", jsonString, partnerAuthToken).toString(), UserDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw new ConflictingException(extractExternalErrorMessage(e));
    }
  }

  private String extractExternalErrorMessage(Exception e) {
    String message = resolveExternalErrorMessage(extractExternalErrorCode(e),
        extractRawExternalErrorMessage(e));
    if (message == null || message.isBlank()) {
      return "Nie udało się utworzyć pracownika.";
    }

    return message.replaceFirst("^HTTP\\s+\\d{3}:\\s*", "");
  }

  private RuntimeException mapExternalException(Exception e) {
    Integer statusCode = extractExternalStatusCode(e);
    String externalCode = extractExternalErrorCode(e);
    String externalMessage =
        resolveExternalErrorMessage(externalCode, extractRawExternalErrorMessage(e));

    if (statusCode != null && statusCode == 400) {
      return new BadRequestException(externalMessage, externalCode);
    }
    if (statusCode != null && statusCode == 409) {
      return new ConflictingException(externalMessage, externalCode, e);
    }

    return new ExternalSystemException(externalMessage, externalCode, statusCode, e);
  }

  private String resolveExternalErrorMessage(String externalCode, String fallbackMessage) {
    if (externalCode != null && !externalCode.isBlank()) {
      return exceptionMessagesService.getMessageByCode(externalCode);
    }
    if (fallbackMessage != null && !fallbackMessage.isBlank()) {
      return sanitizeExternalErrorMessage(fallbackMessage);
    }
    return exceptionMessagesService.getMessageByCode(null);
  }

  private String sanitizeExternalErrorMessage(String fallbackMessage) {
    String message = fallbackMessage.replaceFirst("^HTTP\\s+\\d{3}:\\s*", "");
    String lowerMessage = message.toLowerCase(Locale.ROOT);

    if (lowerMessage.contains("value too long for type character varying")
        || lowerMessage.contains("dataexception")) {
      return EXTERNAL_TEXT_TOO_LONG_ERROR;
    }

    if (lowerMessage.contains("could not execute statement")
        || lowerMessage.contains("org.hibernate")
        || lowerMessage.contains("sql error")
        || lowerMessage.contains("constraint")) {
      return GENERIC_EXTERNAL_SAVE_ERROR;
    }

    return message;
  }

  private String extractRawExternalErrorMessage(Exception e) {
    return e.getLocalizedMessage();
  }

  private String extractExternalErrorCode(Exception e) {
    if (e instanceof ExternalSystemException) {
      return ((ExternalSystemException) e).getCode();
    }
    return null;
  }

  private Integer extractExternalStatusCode(Exception e) {
    if (e instanceof ExternalSystemException) {
      return ((ExternalSystemException) e).getStatusCode();
    }

    String message = e.getMessage();
    if (message != null) {
      if (message.contains("400")) {
        return 400;
      }
      if (message.contains("409")) {
        return 409;
      }
    }
    return null;
  }

  private ExternalSystemException buildExternalSystemException(int statusCode, String responseBody) {
    String message = responseBody;
    String code = null;

    if (responseBody != null && !responseBody.isBlank()) {
      try {
        AbstractErrorDTO error =
            JsonbConfig.getInstance().fromJson(responseBody, AbstractErrorDTO.class);
        if (error != null) {
          if (error.message != null && !error.message.isBlank()) {
            message = error.message;
          }
          if (error.code != null && !error.code.isBlank()) {
            code = error.code;
          }
        }
      } catch (Exception ignored) {
      }
    }

    if (message == null || message.isBlank()) {
      message = "Zewnętrzny system zwrócił HTTP " + statusCode + " bez treści odpowiedzi.";
    }

    return new ExternalSystemException(message, code, statusCode);
  }

  public List<TicketPoolDefinitionDTO> getWholeDay(List<Long> tpdIds) {
    try {
      JsonStructure respJson = post("/v1/ticket-pool-definitions/get-whole-day",
          JsonbConfig.getInstance().toJson(tpdIds), AUTH_TOKEN);
      JsonArray jsonArray = (JsonArray) respJson;
      var resp = new ArrayList<TicketPoolDefinitionDTO>();
      final Jsonb jsonb = JsonbConfig.getInstance();
      jsonArray.forEach(p -> {
        var tpdDTO = jsonb.fromJson(p.toString(), TicketPoolDefinitionDTO.class);
        resp.add(tpdDTO);
      });
      return resp;
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return null;
    }
  }

  public List<Long> getAvailableSightEvents(List<Long> sightEvents) {
    var result = new ArrayList<Long>();
    String json = JsonbConfig.getInstance().toJson(sightEvents);
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure respJson = post("/v1/sight-events/available", json, AUTH_TOKEN);
      JsonArray jsonArray = (JsonArray) respJson;
      List<Long> resp = new ArrayList<>();
      jsonArray.forEach(p -> {
        var id = jsonb.fromJson(p.toString(), Long.class);
        resp.add(id);
      });
      resp.forEach(hptId -> {
        for (Long se : sightEvents) {
          if (hptId.equals(se)) {
            result.add(se);
            break;
          }
        }
      });
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
    }
    return result;
  }

  public List<SightEvent> getSightEventsInDateRange(List<SightEvent> sightEvents, Date fromDate,
      Date toDate) {
    String json = JsonbConfig.getInstance()
        .toJson(sightEvents.stream().map(SightEvent::getHptId).collect(Collectors.toSet()));
    final var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    var urlStr = "/v1/sight-events/in-date-range?fromDate="
        + dateFormat.format(fromDate != null ? fromDate : new Date());
    if (toDate != null) {
      urlStr += "&toDate=" + dateFormat.format(toDate);
    }
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure respJson = post(urlStr, json, AUTH_TOKEN);
      JsonArray jsonArray = (JsonArray) respJson;
      return jsonArray
          .stream()
          .map(jv -> jsonb.fromJson(jv.toString(), SightEventPriceDTO.class))
          .map(idPrice -> {
            for (SightEvent se : sightEvents) {
              if (idPrice.id.equals(se.getHptId())) {
                se.setMinPrice(idPrice.price);
                se.setMinDiscountPrice(idPrice.discountPrice);
                return se;
              }
            }
            return null; // never happens
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return null;
    }
  }

  private JsonStructure post(String path, String json, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    var conn = (HttpURLConnection) url.openConnection();
    conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
    conn.setReadTimeout(READ_TIMEOUT_MS);

      conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO, "Sending POST request to url: " + url);
    logger.log(System.Logger.Level.DEBUG,
        "Sending POST request to url: " + url + " with body: " + json);
    conn.setRequestProperty("Authorization", "Bearer " + authToken);
    conn.setDoOutput(true);
    var os = conn.getOutputStream();
    PrintWriter printWriter = new PrintWriter(os);
    printWriter.append(json);
    printWriter.close();
      var respCode = conn.getResponseCode();

      InputStream is;
      if (respCode >= 400) {
          is = conn.getErrorStream();
          String respString = (is != null) ? IOUtils.toString(is) : "";
          if (is != null) is.close();
          logger.log(System.Logger.Level.WARNING,
              "Server responded with error code: " + respCode + " and body: " + respString);
          throw buildExternalSystemException(respCode, respString);
      }

      is = conn.getInputStream();
      var resp = JsonbConfig.getInstance().fromJson(is, JsonStructure.class);
      is.close();
    logger.log(System.Logger.Level.INFO, "Server responded with code: " + respCode);
    logger.log(System.Logger.Level.DEBUG, "Server responded with body: " + resp);
    return resp;
  }

  private JsonStructure put(String path, String json, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
      conn.setReadTimeout(READ_TIMEOUT_MS);

    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO, "Sending PUT request to url: " + url);
    logger.log(System.Logger.Level.DEBUG,
        "Sending PUT request to url: " + url + " with body: " + json);
    conn.setRequestMethod("PUT");
    conn.setRequestProperty("Authorization", "Bearer " + authToken);
    if (json != null) {
      conn.setDoOutput(true);
      var os = conn.getOutputStream();
      PrintWriter printWriter = new PrintWriter(os);
      printWriter.append(json);
      printWriter.close();
    }
      var respCode = conn.getResponseCode();

      InputStream is;
      if (respCode >= 400) {
          is = conn.getErrorStream();
          String respString = (is != null) ? IOUtils.toString(is) : "";
          if (is != null) is.close();
          throw buildExternalSystemException(respCode, respString);
      }

      is = conn.getInputStream();
      var resp = JsonbConfig.getInstance().fromJson(is, JsonStructure.class);
      is.close();
    logger.log(System.Logger.Level.INFO, "Server responded with code: " + respCode);
    logger.log(System.Logger.Level.DEBUG, "Server responded with body: " + resp);
    return resp;
  }

  private int delete(String path, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
      conn.setReadTimeout(READ_TIMEOUT_MS);

      conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO, "Sending DELETE request to url: " + url);
    conn.setRequestMethod("DELETE");
    conn.setRequestProperty("Authorization", "Bearer " + authToken);
      //conn.setDoOutput(true);
    conn.connect();
      var respCode = conn.getResponseCode();

      InputStream is;
      if (respCode >= 400) {
          is = conn.getErrorStream();
          String respString = (is != null) ? IOUtils.toString(is) : "";
          if (is != null) is.close();
          throw buildExternalSystemException(respCode, respString);
      }

      is = conn.getInputStream();
      logger.log(System.Logger.Level.INFO, "Server responded with code: " + respCode);
      is.close();


    if (respCode != NO_CONTENT.getStatusCode() && respCode != OK.getStatusCode()) {
      throw new CannotDeleteSightEventFromExternalSystemException();
    }
    return respCode;
  }

  private JsonStructure get(String path, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
      conn.setReadTimeout(READ_TIMEOUT_MS);

      conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO, "Sending GET request to url: " + url);
    conn.setRequestMethod("GET");
    if (authToken != null) {
      conn.setRequestProperty("Authorization", "Bearer " + authToken);
    }
    //conn.setDoOutput(true);
    conn.connect();
      var respCode = conn.getResponseCode();
      logger.log(System.Logger.Level.INFO, "Server responded with code: " + respCode);

      InputStream is;
      if (respCode >= 400) {
          is = conn.getErrorStream();
          String resp = (is != null) ? IOUtils.toString(is) : "";
          if (is != null) is.close();
          throw buildExternalSystemException(respCode, resp);
      }

      is = conn.getInputStream();
      String resp = IOUtils.toString(is);
      is.close();
    logger.log(System.Logger.Level.DEBUG, "Server responded with body: " + resp);
    return JsonbConfig.getInstance().fromJson(resp, JsonStructure.class);
  }

  public List<LocalDate> checkAvailableDates(Long tpdId, LocalDate fromDate, LocalDate toDate) {
    try {
      String resp = get(("/v1/ticket-pool-definitions/" + tpdId + "/available-dates?fromDate="
          + fromDate + "&toDate=" + toDate), AUTH_TOKEN).toString();
      @SuppressWarnings("unchecked")
      Collection<String> coll = JsonbConfig.getInstance().fromJson(resp, Collection.class);
      return coll.stream().map(LocalDate::parse).collect(Collectors.toList());
    } catch (JsonbException | IOException e) {
      logger.log(WARNING, "Failed", e);
      throw new ConflictingException(e.getLocalizedMessage());
    }
  }

  public static class ListOfTicketPoolDefinitionDTOs extends ArrayList<TicketPoolDefinitionDTO> {
    private static final long serialVersionUID = 6554050835860859011L;
  }

  public TicketPoolDefinitionDTO updateTicketPoolDefinition(TicketPoolDefinitionDTO dto,
      String partnerAuthToken) {
    try {
      Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json =
          put("/v1/ticket-pool-definitions/" + dto.id, jsonb.toJson(dto), partnerAuthToken);

      return jsonb.fromJson(json.toString(), TicketPoolDefinitionDTO.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw mapExternalException(e);
    }
  }

  public static class ListOfTicketDefinitionDTOs extends ArrayList<TicketDefinitionDTO> {
    private static final long serialVersionUID = 6554050835860859011L;
  }

  public List<TicketDefinitionDTO> updateTicketDefinition(TicketDefinitionDTO dto,
      String partnerAuthToken) {
    try {
      Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json =
          put("/v1/ticket-definitions/" + dto.id, jsonb.toJson(dto), partnerAuthToken);
      return jsonb.fromJson(json.toString(), ListOfTicketDefinitionDTOs.class);
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      throw mapExternalException(e);
    }
  }

  public List<TicketDefinitionDTO> getTicketDefinitions(Set<Long> atnaIds) {
    String url = "/v1/ticket-definitions";
    if (atnaIds != null) {
      url += "?atnaIds=";
      url += atnaIds.stream().map(Objects::toString).collect(joining("&atnaIds="));
    }
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = get(url, null);
      JsonArray jsonArray = (JsonArray) json;
      List<TicketDefinitionDTO> dtos = new ArrayList<>();
      jsonArray.forEach(p -> {
        var dto = jsonb.fromJson(p.toString(), TicketDefinitionDTO.class);
        dtos.add(dto);
      });
      return dtos;
    } catch (Exception e) {
      logger.log(WARNING, "Failed", e);
      return null;
    }
  }

    public List<TicketDefinitionDTO> getTicketDefinitionsMarket(Set<Long> atnaIds) {
        String url = "/v1/ticket-definitions/market";
        if (atnaIds != null) {
            url += "?atnaIds=" + atnaIds.stream().map(Objects::toString).collect(joining("&atnaIds="));
        }
        try {
            JsonStructure json = get(url, null);
            JsonArray arr = (JsonArray) json;
            List<TicketDefinitionDTO> dtos = new ArrayList<>();
            final Jsonb jsonb = JsonbConfig.getInstance();
            arr.forEach(p -> dtos.add(jsonb.fromJson(p.toString(), TicketDefinitionDTO.class)));
            return dtos;
        } catch (Exception e) {
            logger.log(WARNING, "getTicketDefinitionsMarket failed: " + url, e);
            return null;
        }
    }

    public void deleteUsherForPartner(long usherId, String partnerAuthToken) {
        try {
            delete("/v1/partners/ushers/" + usherId, partnerAuthToken);
        } catch (Exception e) {
            logger.log(System.Logger.Level.WARNING, "Failed", e);
            throw new ConflictingException(
                    "Nie udało się usunąć biletera w zewnętrznym systemie.");
        }
    }

}
