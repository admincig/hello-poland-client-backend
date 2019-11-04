package pl.hellopoland.rest.market;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.rest.dto.OrderDateEntryORO;
import pl.hellopoland.rest.dto.OrderDateEntryOnListingORO;
import pl.hellopoland.service.api.market.OrderServiceMarketAPI;

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
