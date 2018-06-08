package pl.hellopoland.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import pl.hellopoland.dto.booking.Booking;
import pl.hellopoland.dto.booking.Ticket;
import pl.hellopoland.order.OrderDetails;
import pl.hellopoland.order.OrderEntry;

public class HelloTicket {

  public HelloTicket(String url) {
    this.url = url;
  }

  private System.Logger logger = System.getLogger(HelloTicket.class.getName());
  private String url;

  public JsonObject book(OrderDetails details, List<OrderEntry> orderEntries) {
    Booking booking = new Booking();
    booking.customerEmail = details.getEmail();
    booking.customerName = details.getFirstName() + " " + details.getLastName();
    List<Ticket> ticketBookings = orderEntries.stream().map(oe -> {
      Ticket t = new Ticket();
      t.ticketDefinitionId = oe.getExternalId();
      t.numberOfTickets = oe.getQuantity().longValue();
      return t;
    }).collect(Collectors.toList());
    booking.ticketBookings = ticketBookings.toArray(new Ticket[ticketBookings.size()]);
    var json = prepareJson(booking);
    try {
      return post("/api/v1/bookings", json);
    } catch (IOException e) {
      logger.log(System.Logger.Level.WARNING, e);
      return null;
    }
  }

  public JsonObject confirm(Long orderId) {
    try {
      return put("/api/v1/bookings/buy/" + orderId, null);
    } catch (IOException e) {
      logger.log(System.Logger.Level.WARNING, e);
      return null;
    }
  }

  private JsonObject post(String path, JsonObject json) throws IOException {
    URL url = new URL(this.url + path);
    var conn = url.openConnection();
    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO,
        "Sending POST request to url: " + url + " with body: " + json);
    conn.setRequestProperty("Authorization",
        "Bearer eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.");
    conn.setDoOutput(true);
    var os = conn.getOutputStream();
    Json.createWriter(os).writeObject(json);
    var is = conn.getInputStream();
    var resp = Json.createReader(is).readObject();
    logger.log(System.Logger.Level.INFO, "Server responded with body: " + resp);
    return resp;
  }

  private JsonObject put(String path, JsonObject json) throws IOException {
    URL url = new URL(this.url + path);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    logger.log(System.Logger.Level.INFO,
        "Sending PUT request to url: " + url + " with body: " + json);
    conn.setRequestMethod("PUT");
    conn.setRequestProperty("Authorization",
        "Bearer eyJhbGciOiJub25lIn0.eyJzdWIiOiI1RDU1NTEwOURBM0Y5RUQwMEVFRkQyNTY2MDMwRUQ3MjJBNEQ3NzAwREU2MDA2NjQ5NzhBNjIwOTRCNUVFN0Y0In0.");
    if (json != null) {
      conn.setDoOutput(true);
      var os = conn.getOutputStream();
      Json.createWriter(os).writeObject(json);
    }
    var is = conn.getInputStream();
    var resp = Json.createReader(is).readObject();
    logger.log(System.Logger.Level.INFO, "Server responded with body: " + resp);
    return resp;
  }

  // TODO change for jsonb in JEE8
  private JsonObject prepareJson(Booking booking) {
    var ticketBuilder = Json.createArrayBuilder();
    for (Ticket t : booking.ticketBookings) {
      ticketBuilder.add(Json.createObjectBuilder().add("ticketDefinitionId", t.ticketDefinitionId)
          .add("numberOfTickets", t.numberOfTickets).build());
    } ;
    return Json.createObjectBuilder().add("customerEmail", booking.customerEmail)
        .add("customerName", booking.customerName).add("ticketBookings", ticketBuilder.build())
        .build();
  }

}
