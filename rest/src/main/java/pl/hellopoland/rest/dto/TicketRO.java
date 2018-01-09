package pl.hellopoland.rest.dto;

import pl.hellopoland.sight.Ticket;

public class TicketRO {
  public Long id;
  public String name;
  public Integer price;

  public TicketRO(Ticket t) {
    this.id = t.getId();
    this.name = t.getName();
    this.price = t.getPrice();
  }
}
