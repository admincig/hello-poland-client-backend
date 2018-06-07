package pl.hellopoland.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import pl.hellopoland.dto.Booking;
import pl.hellopoland.order.OrderDetails;
import pl.hellopoland.order.OrderEntry;

public class HelloTicket {

  public HelloTicket(String url) {
    this.url = url;
  }

  private System.Logger logger = System.getLogger(HelloTicket.class.getName());
  private String url;

  public JsonObject book(OrderDetails details, List<OrderEntry> orderEntries) throws IOException {
    Booking booking = new Booking();
    booking.customerEmail = details.getEmail();
    booking.customerName = details.getFirstName() + " " + details.getLastName();
    booking.ticketBookings = orderEntries.stream().map(oe -> {
      Booking.Ticket t = booking.new Ticket();
      t.ticketDefinitionId = oe.getExternalId();
      t.numberOfTickets = oe.getQuantity().longValue();
      return t;
    }).collect(Collectors.toList());
    return post("/bookings", booking);
  }

  private JsonObject post(String path, Booking booking) throws IOException {
    var json = prepareJson(booking);
    URL url = new URL(this.url + path);
    var conn = url.openConnection();
    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    logger.log(System.Logger.Level.INFO,
        "Sending POST request to url: " + url.getPath() + " with body: " + json);
    var os = conn.getOutputStream();
    Json.createWriter(os).writeObject(json);
    var is = conn.getInputStream();
    var resp = Json.createReader(is).readObject();
    logger.log(System.Logger.Level.INFO, "Server responded with body: " + resp);
    return resp;
  }

  // TODO change for jsonb in JEE8
  private JsonObject prepareJson(Booking booking) {
    var ticketBuilder = Json.createArrayBuilder();
    booking.ticketBookings.forEach(t -> {
      ticketBuilder.add(Json.createObjectBuilder().add("ticketDefinitionId", t.ticketDefinitionId)
          .add("numberOfTickets", t.numberOfTickets).build());
    });
    return Json.createObjectBuilder().add("customerEmail", booking.customerEmail)
        .add("customerName", booking.customerName).add("ticketBookings", ticketBuilder.build())
        .build();
  }

}
