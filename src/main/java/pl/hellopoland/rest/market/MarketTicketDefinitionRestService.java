package pl.hellopoland.rest.market;

import pl.hellopoland.rest.dto.OrderDateEntryORO;
import pl.hellopoland.rest.dto.OrderDateEntryOnListingORO;
import pl.hellopoland.service.api.market.OrderServiceMarketAPI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/market/tickets")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketTicketDefinitionRestService {

  @Inject
  OrderServiceMarketAPI service;

  @GET
  public List<OrderDateEntryOnListingORO> tickets() {
    return service.getActiveTicketGroupsForLoggedUser();
  }

  @GET
  @Path("/{id}")
  public OrderDateEntryORO ticket(@PathParam("id") Long id) {
    return service.getTicketGroup(id);
  }

  @DELETE
  @Path("/{id}")
  public void deleteTicket(@PathParam("id") Long id) {
    service.deleteTicketGroup(id);
  }

  @GET
  @Path("/archive")
  public List<OrderDateEntryOnListingORO> archivedTickets() {
    return service.getArchivedTicketGroupsForLoggedUser();
  }

}
