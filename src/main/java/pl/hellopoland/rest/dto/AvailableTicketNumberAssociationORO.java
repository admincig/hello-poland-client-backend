package pl.hellopoland.rest.dto;

import java.util.ArrayList;
import java.util.List;
import pl.hellopoland.dto.AvailableTicketNumberAssociationDTO;

public class AvailableTicketNumberAssociationORO {
  public List<Object> ticketPools;

  public AvailableTicketNumberAssociationORO() {}

  public AvailableTicketNumberAssociationORO(AvailableTicketNumberAssociationDTO associationDTO) {
    this.ticketPools = new ArrayList<>();
    if (associationDTO.ticketPoolDefinitions != null
        && !associationDTO.ticketPoolDefinitions.isEmpty()) {
      ticketPools.addAll(associationDTO.ticketPoolDefinitions);
    }
    if (associationDTO.ticketPools != null && !associationDTO.ticketPools.isEmpty()) {
      ticketPools.addAll(associationDTO.ticketPools);
    }
  }
}
