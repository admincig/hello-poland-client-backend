package pl.hellopoland.rest.dto;

import java.util.Date;

public class HelpdeskSalesRowORO {

  public Long bookingId;
  public Date purchaseDate;
  public Date eventDate;
  public String customerName;
  public String customerEmail;
  public String status;
  public String hash;
  public String sightEventName;
  public String objectName;
  public Integer ticketPrice;

  public HelpdeskSalesRowORO(Long bookingId, Date purchaseDate, Date eventDate,
      String customerName, String customerEmail, String status, String hash,
      String sightEventName, String objectName, Integer ticketPrice) {
    this.bookingId = bookingId;
    this.purchaseDate = purchaseDate;
    this.eventDate = eventDate;
    this.customerName = customerName;
    this.customerEmail = customerEmail;
    this.status = status;
    this.hash = hash;
    this.sightEventName = sightEventName;
    this.objectName = objectName;
    this.ticketPrice = ticketPrice;
  }
}
