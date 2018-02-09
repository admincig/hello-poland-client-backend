package pl.hellopoland.rest.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import pl.hellopoland.sight.Sight;

public class SightRO extends SightOnListingRO {
  public String lead;
  public String score;
  public SightLocationRO location;
  public String email;
  public String phone;
  public Collection<TicketRO> tickets;
  public Collection<OpeningHoursRO> openingHours;
  public Collection<AgreementRO> agreements;

  public SightRO(Sight s) {
    super(s);
    this.lead = s.getLead();
    this.score = String.format("%.1f", s.getScore());
    this.tickets = s.getTickets().stream().map(TicketRO::new).collect(Collectors.toList());
    this.openingHours =
        s.getOpeningHours().stream().map(OpeningHoursRO::new).collect(Collectors.toList());
    if (s.getLocation() != null) {
      this.location = new SightLocationRO(s.getLocation());
    }
    this.agreements = s.getAgreements().stream().map(AgreementRO::new).collect(Collectors.toList());
    this.email = s.getEmail();
    this.phone = s.getPhone();
  }

}
