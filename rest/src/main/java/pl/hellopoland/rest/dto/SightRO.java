package pl.hellopoland.rest.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import pl.hellopoland.model.Sight;

public class SightRO extends SightOnListingRO {
  public String lead;
  public String description;
  public Float score;
  public Collection<TicketRO> tickets;

  public SightRO(Sight f) {
    super(f);
    this.lead = f.getLead();
    this.description = f.getDescription();
    this.score = f.getScore();
    this.tickets = f.getTickets().stream().map(TicketRO::new).collect(Collectors.toList());
  }

}
