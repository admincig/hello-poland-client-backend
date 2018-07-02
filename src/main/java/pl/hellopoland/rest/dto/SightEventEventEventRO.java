package pl.hellopoland.rest.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import pl.hellopoland.sight.SightEvent;

public class SightEventEventEventRO extends SightEventEventOnListingRO {

  public String lead;
  public String score;
  public String email;
  public String phone;
  public Collection<TicketRO> tickets;
  public Collection<OpeningHoursRO> openingHours;
  public Collection<AgreementRO> agreements;

  public SightEventEventEventRO(SightEvent s) {
    super(s);
    this.lead = s.getLead();
    this.score = String.format("%.1f", s.getScore());
    this.tickets = s.getTickets().stream().map(TicketRO::new).collect(Collectors.toList());
    this.openingHours =
        s.getOpeningHours().stream().map(OpeningHoursRO::new).collect(Collectors.toList());
    this.agreements = s.getAgreements().stream().map(AgreementRO::new).collect(Collectors.toList());
    this.email = s.getEmail();
    this.phone = s.getPhone();
  }

}
