package pl.hellopoland.rest.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import pl.hellopoland.sight.Sight;

public class SightRO extends SightOnListingRO {
  public String lead;
  public String description;
  public String score;
  public LocationRO location;
  public Collection<TicketRO> tickets;
  public Collection<OpeningHoursRO> openingHours;

  public SightRO(Sight f) {
    super(f);
    this.lead = f.getLead();
    this.description = f.getDescription();
    this.score = String.format("%.1f", f.getScore());
    this.tickets = f.getTickets().stream().map(TicketRO::new).collect(Collectors.toList());
    this.openingHours =
        f.getOpeningHours().stream().map(OpeningHoursRO::new).collect(Collectors.toList());
    if (f.getLocation() != null) {
      this.location = new LocationRO(f.getLocation());
    }
  }

}
