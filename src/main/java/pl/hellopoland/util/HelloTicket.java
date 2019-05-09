package pl.hellopoland.util;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger.Level;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.json.JsonArray;
import javax.json.JsonStructure;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;
import pl.hellopoland.bo.Order;
import pl.hellopoland.bo.OrderDateEntry;
import pl.hellopoland.bo.OrderDetails;
import pl.hellopoland.bo.OrderEntry;
import pl.hellopoland.bo.OrderSightEntry;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.dto.AvailableTicketNumberAssociationDTO;
import pl.hellopoland.dto.EmailSendingReportDTO;
import pl.hellopoland.dto.FileDescriptorDTO;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.dto.booking.BookingDTO;
import pl.hellopoland.dto.booking.TicketDTO;
import pl.hellopoland.dto.booking.TicketOrderDTO;
import pl.hellopoland.exception.badrequest.BadRequestException;
import pl.hellopoland.exception.conflict.CannotDeleteSightEventFromExternalSystemException;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.email.EmailSendingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.rest.JsonbConfig;

public class HelloTicket {

  public HelloTicket(String url) {
    this.url = url;
  }

  private System.Logger logger = System.getLogger(HelloTicket.class.getName());
  private String url;

  private static final String AUTH_TOKEN =
      "eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.";

  public JsonStructure book(OrderDetails details, List<OrderEntry> orderEntries) {
    BookingDTO booking = new BookingDTO();
    booking.customerEmail = details.getEmail();
    booking.customerName = details.getFirstName() + " " + details.getLastName();
    List<TicketOrderDTO> ticketBookings = orderEntries.stream().map(oe -> {
      TicketOrderDTO t = new TicketOrderDTO();
      t.ticketDefinitionId = oe.getExternalDefinitionId();
      t.ticketPoolDefinitionId = oe.getPoolId();
      t.numberOfTickets = oe.getQuantity().longValue();
      t.date = oe.getDateEntry().getDate();
      return t;
    }).collect(Collectors.toList());
    booking.ticketBookings = ticketBookings;
    // booking.sightEventPdfAttachments = orderEntries.stream()
    // .map(oe -> oe.getDateEntry().getSightEntry().getSightEvent().getPdfAttachment())
    // .filter(pdf -> pdf != null).distinct().map(DtoMapper::getFullDTO)
    // .collect(Collectors.toSet());
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
            ose.setWholeDay(ticket.wholeDay);
            break;
          }
        }
      }
      return resp;
    } catch (IOException e) {
      logger.log(System.Logger.Level.WARNING, e);
      return null;
    }
  }

  public JsonStructure confirm(String serialNumber, List<OrderEntry> orderEntries) {
    try {
      var p24OrderId = Optional.ofNullable(orderEntries.get(0)).map(OrderEntry::getDateEntry)
          .map(OrderDateEntry::getSightEntry).map(OrderSightEntry::getOrder)
          .map(Order::getP24OrderId).orElse("");
      var p24Currency = Optional.ofNullable(orderEntries.get(0)).map(OrderEntry::getDateEntry)
          .map(OrderDateEntry::getSightEntry).map(OrderSightEntry::getOrder)
          .map(Order::getP24Currency).orElse("");
      var resp = put("/v1/bookings/buy/" + serialNumber + "/" + p24OrderId + "/" + p24Currency,
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
      logger.log(System.Logger.Level.WARNING, e);
      return null;
    }
  }

  public SightEventDTO addSightEvent(SightEventDTO dto, String partnerAuthToken) {
    String json = JsonbConfig.getInstance().toJson(dto);

    try {
      return JsonbConfig.getInstance().fromJson(
          post("/v1/sight-events", json, partnerAuthToken).toString(), SightEventDTO.class);
    } catch (IOException e) {
      logger.log(Level.ERROR, e);
    }

    return null;
  }

  public TicketDefinitionDTO addTicketDefinition(TicketDefinitionDTO dto, String partnerAuthToken) {
    String json = JsonbConfig.getInstance().toJson(dto);

    try {
      return JsonbConfig.getInstance().fromJson(
          post("/v1/ticket-definitions", json, partnerAuthToken).toString(),
          TicketDefinitionDTO.class);
    } catch (IOException e) {
      logger.log(Level.ERROR, e);
    }

    return null;
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
    } catch (IOException e) {
      logger.log(Level.ERROR, e);
    }

    return null;
  }

  private JsonStructure post(String path, String json, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    var conn = url.openConnection();
    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO,
        "Sending POST request to url: " + url + " with body: " + json);
    conn.setRequestProperty("Authorization", "Bearer " + authToken);
    conn.setDoOutput(true);
    var os = conn.getOutputStream();
    PrintWriter printWriter = new PrintWriter(os);
    printWriter.append(json);
    printWriter.close();
    var is = conn.getInputStream();
    var resp = JsonbConfig.getInstance().fromJson(is, JsonStructure.class);
    logger.log(System.Logger.Level.INFO, "Server responded with body: " + resp);
    return resp;
  }

  private JsonStructure put(String path, String json, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO,
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
    var is = conn.getInputStream();
    var resp = JsonbConfig.getInstance().fromJson(is, JsonStructure.class);
    logger.log(System.Logger.Level.INFO, "Server responded with body: " + resp);
    return resp;
  }

  private int delete(String path, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO, "Sending DELETE request to url: " + url);
    conn.setRequestMethod("DELETE");
    conn.setRequestProperty("Authorization", "Bearer " + authToken);
    conn.setDoOutput(true);
    conn.connect();
    var is = conn.getInputStream();
    int responseCode = conn.getResponseCode();
    logger.log(System.Logger.Level.INFO, "Server responded with code: " + responseCode);
    is.close();

    if (responseCode != NO_CONTENT.getStatusCode()) {
      throw new CannotDeleteSightEventFromExternalSystemException();
    }
    return responseCode;
  }

  private JsonStructure get(String path, String authToken) throws IOException {
    URL url = new URL(this.url + path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO, "Sending GET request to url: " + url);
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Bearer " + authToken);
    conn.setDoOutput(true);
    conn.connect();
    var is = conn.getInputStream();
    int responseCode = conn.getResponseCode();
    logger.log(System.Logger.Level.INFO, "Server responded with code: " + responseCode);
    var resp = JsonbConfig.getInstance().fromJson(is, JsonStructure.class);
    is.close();
    return resp;
  }

  public List<TicketPoolDefinitionDTO> getTicketPoolDefinitions(String partnerAuthToken) {
    try {
      final Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = get("/v1/ticket-pool-definitions", partnerAuthToken);
      JsonArray jsonArray = (JsonArray) json;
      List<TicketPoolDefinitionDTO> dtos = new ArrayList<>();
      jsonArray.forEach(p -> {
        var dto = jsonb.fromJson(p.toString(), TicketPoolDefinitionDTO.class);
        dtos.add(dto);
      });
      return dtos;
    } catch (Exception e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      return null;
    }
  }

  public TicketPoolDefinitionDTO addTicketPoolDefinition(TicketPoolDefinitionDTO dto,
      String partnerAuthToken) {
    try {
      Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = post("/v1/ticket-pool-definitions", jsonb.toJson(dto), partnerAuthToken);
      return jsonb.fromJson(json.toString(), TicketPoolDefinitionDTO.class);
    } catch (Exception e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      if (e.getMessage() != null && e.getMessage().contains("400")) {
        throw new BadRequestException("TicketPoolDefinition must have tickets definitions.");
      }
      if (e.getMessage() != null && e.getMessage().contains("409")) {
        throw new ConflictingException("Bad availableTicketsNumber limit combination.");
      }
      return null;
    }
  }

  public TicketPoolDefinitionDTO getTicketPoolDefinition(String hptToken, Long id) {
    try {
      return JsonbConfig.getInstance().fromJson(
          get("/v1/ticket-pool-definitions/" + id, hptToken).toString(),
          TicketPoolDefinitionDTO.class);
    } catch (JsonbException | IOException e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
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
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      return null;
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
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      throw new ConflictingException(e.getLocalizedMessage());
    }
  }

  public PartnerDTO addPartner(PartnerDTO dto, String hptToken) {
    try {
      Jsonb jsonb = JsonbConfig.getInstance();
      JsonStructure json = post("/v1/helpdesk/partners", jsonb.toJson(dto), hptToken);
      return jsonb.fromJson(json.toString(), PartnerDTO.class);
    } catch (Exception e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      return null;
    }
  }

  public void stopSale(String hptToken, Long sightEventHptId, Long ticketPoolDefId, Date date) {
    try {
      var dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String dateString = dateFormat.format(date);
      delete("/v1/sight-events/" + sightEventHptId + "/sale?tpdId=" + ticketPoolDefId + "&date="
          + dateString, hptToken);
    } catch (Exception e) {
      logger
          .log(System.Logger.Level.WARNING,
              "Failed: cannot find sight event for id=" + sightEventHptId + ", ticketPoolDefId="
                  + ticketPoolDefId + " and date=" + SimpleDateFormat.getInstance().format(date),
              e);
      throw new ConflictingException("Brak wydarzenia w danym dniu");
    }
  }

  public void changePartnerPassword(UserAuthDTO userAuthDTO, String hptToken) {
    try {
      String json = JsonbConfig.getInstance().toJson(userAuthDTO);
      put("/v1/users/me/password", json, hptToken);
    } catch (Exception e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      throw new ConflictingException("Zmiana hasła w zewnętrznym systemie nie powiodła się");
    }
  }

  public void changeUsherPassword(long usherId, UserAuthDTO userAuthDTO, String hptToken) {
    try {
      String json = JsonbConfig.getInstance().toJson(userAuthDTO);
      put("/v1/users/" + usherId + "/password", json, hptToken);
    } catch (Exception e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
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
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      return null;
    }
  }

  public UserDTO getUsherForPartner(long usherId, String partnerAuthToken) {
    try {
      return JsonbConfig.getInstance().fromJson(
          get("/v1/partners/ushers/" + usherId, partnerAuthToken).toString(), UserDTO.class);
    } catch (Exception e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
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
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      throw new EmailSendingException();
    }
  }

  public EmailSendingReportDTO sendTicketsCopyByAdmin(String serialNumber, String hptToken) {
    try {
      return JsonbConfig.getInstance().fromJson(
          get("/v1/helpdesk/bookings/" + serialNumber + "/sendTicketCopy", hptToken).toString(),
          EmailSendingReportDTO.class);
    } catch (Exception e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      throw new EmailSendingException();
    }
  }

  public void addPdfToSightEvent(Long sightEventHptId, FileDescriptorDTO pdfDto,
      String partnerAuthToken) {
    String pdfJsonString = JsonbConfig.getInstance().toJson(pdfDto);
    try {
      put("/v1/sight-events/" + sightEventHptId + "/pdf", pdfJsonString, partnerAuthToken);
    } catch (IOException e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      throw new ConflictingException(
          "Wystąpił problem podczas zapisu pdf'a w zewnętrznym systemie.");
    }
  }

  public void deletePdfFromSightEvent(SightEvent sightEvent, String partnerAuthToken) {
    try {
      delete("/v1/sight-events/" + sightEvent.getHptId() + "/pdf/"
          + sightEvent.getPdfAttachment().getPath(), partnerAuthToken);
    } catch (IOException e) {
      logger.log(System.Logger.Level.WARNING, "Failed", e);
      throw new ConflictingException(
          "Wystąpił problem podczas usówania pdf'a w zewnętrznym systemie.");
    }
  }

}
