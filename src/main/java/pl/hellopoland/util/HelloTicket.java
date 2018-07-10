package pl.hellopoland.util;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger.Level;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;
import pl.hellopoland.dto.booking.Booking;
import pl.hellopoland.dto.booking.Ticket;
import pl.hellopoland.exception.conflict.CannotDeleteSightEventFromExternalSystemException;
import pl.hellopoland.order.OrderDetails;
import pl.hellopoland.order.OrderEntry;
import pl.hellopoland.sight.SightEvent;

public class HelloTicket {

  public HelloTicket(String url) {
    this.url = url;
    this.df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  }

  private System.Logger logger = System.getLogger(HelloTicket.class.getName());
  private String url;
  private DateFormat df;

  public JsonObject book(OrderDetails details, List<OrderEntry> orderEntries) {
    Booking booking = new Booking();
    booking.customerEmail = details.getEmail();
    booking.customerName = details.getFirstName() + " " + details.getLastName();
    List<Ticket> ticketBookings = orderEntries.stream().map(oe -> {
      Ticket t = new Ticket();
      t.ticketDefinitionId = oe.getExternalDefinitionId();
      t.numberOfTickets = oe.getQuantity().longValue();
      t.date = oe.getDateEntry().getDate();
      return t;
    }).collect(Collectors.toList());
    booking.ticketBookings = ticketBookings;
    var json = JsonbBuilder.create().toJson(booking);
    try {
      String authToken =
          "eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.";
      var resp = post("/v1/bookings", json, authToken);
      String serialNumber = resp.getString("serialNumber");
      boolean serialNumberSetAlready = false;
      JsonArray tickets = resp.getJsonArray("tickets");
      for (var oe : orderEntries) {
        if (!serialNumberSetAlready) {
          oe.getDateEntry().getSightEntry().setSerialNumber(serialNumber);
        }
        oe.getDateEntry().getSightEntry().setSerialNumber(serialNumber);
        for (var iter = tickets.iterator(); iter.hasNext(); ) {
          JsonObject ticket = (JsonObject) iter.next();
          if (oe.getExternalDefinitionId().intValue() == ticket.getInt("definitionId")
              && oe.getDateEntry().getDate().compareTo(df.parse(ticket.getString("date"))) == 0) {
            oe.setExternalId((long) ticket.getInt("id"));
            break;
          }
        }
      }
      return resp;
    } catch (IOException | ParseException e) {
      logger.log(System.Logger.Level.WARNING, e);
      return null;
    }
  }

  public JsonObject confirm(String serialNumber, List<OrderEntry> orderEntries) {
    try {
      String authToken =
          "eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.";
      var resp = put("/v1/bookings/buy/" + serialNumber, null, authToken);
      JsonArray tickets = resp.getJsonArray("tickets");
      for (var oe : orderEntries) {
        for (var iter = tickets.iterator(); iter.hasNext(); ) {
          JsonObject ticket = (JsonObject) iter.next();
          Date date1 = df.parse(ticket.getString("date"));
          Date date2 = oe.getDateEntry().getDate();
          if (date2.compareTo(date1) == 0
              && oe.getExternalDefinitionId().intValue() == ticket.getInt("definitionId")) {
            oe.addNumber(ticket.getString("serialNumber"));
          }
        }
      }
      return resp;
    } catch (IOException | ParseException e) {
      logger.log(System.Logger.Level.WARNING, e);
      return null;
    }
  }

  public pl.hellopoland.dto.SightEvent addSightEvent(pl.hellopoland.dto.SightEvent dto,
      String partnerAuthToken) {
    String json = JsonbBuilder.create().toJson(dto);

    try {
      return JsonbBuilder.create().fromJson(
          post("/v1/sight-events", json, partnerAuthToken).toString(),
          pl.hellopoland.dto.SightEvent.class);
    } catch (IOException e) {
      logger.log(Level.ERROR, e);
    }

    return null;
  }

  public pl.hellopoland.dto.TicketDefinition addTicketDefinition(
      pl.hellopoland.dto.TicketDefinition dto, String partnerAuthToken) {
    String json = JsonbBuilder.create().toJson(dto);

    try {
      return JsonbBuilder.create().fromJson(
          post("/v1/ticket-definitions", json, partnerAuthToken).toString(),
          pl.hellopoland.dto.TicketDefinition.class);
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

  public pl.hellopoland.dto.SightEvent updateSightEvent(pl.hellopoland.dto.SightEvent sightEvent,
      String partnerAuthToken) {
    String sightEventJson = JsonbBuilder.create().toJson(sightEvent);

    try {
      return JsonbBuilder.create().fromJson(
          put("/v1/sight-events/" + sightEvent.id, sightEventJson, partnerAuthToken).toString(),
          pl.hellopoland.dto.SightEvent.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private JsonObject post(String path, String json, String authToken) throws IOException {
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
    var resp = Json.createReader(is).readObject();
    logger.log(System.Logger.Level.INFO, "Server responded with body: " + resp);
    return resp;
  }

  private JsonObject put(String path, String json, String authToken) throws IOException {
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
    var resp = Json.createReader(is).readObject();
    logger.log(System.Logger.Level.INFO, "Server responded with body: " + resp);
    return resp;
  }

  private void delete(String path, String authToken) throws IOException {
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
  }
}
