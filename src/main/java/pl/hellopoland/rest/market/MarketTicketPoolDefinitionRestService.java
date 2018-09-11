package pl.hellopoland.rest.market;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.annotation.DateTimeFormat;
import pl.hellopoland.dto.AvailableTicketNumberAssociationDTO;
import pl.hellopoland.service.api.market.SightEventServiceMarketAPI;

@Path("/market/ticket-pool-definitions")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketTicketPoolDefinitionRestService {

  @Inject
  SightEventServiceMarketAPI service;

  @GET
  @Path("/{id}/availability")
  public List<AvailableTicketNumberAssociationDTO> checkAvailability(@PathParam("id") Long id,
      @QueryParam("date") @DateTimeFormat final Date date) {
    return service.checkAvailability(id, date);
  }

}
