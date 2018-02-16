package pl.hellopoland.rest;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.order.OrderService;
import pl.hellopoland.rest.dto.LoginIRO;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.rest.dto.OrderORO;
import pl.hellopoland.rest.dto.OrderSightDateEntryORO;
import pl.hellopoland.rest.dto.OrderSightDateEntryOnListingORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.SightOnListingRO;
import pl.hellopoland.rest.dto.SightRO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.SightService;
import pl.hellopoland.user.UserService;
import pl.hellopoland.util.PagedEntityCollection;
import pl.hellopoland.util.Triplet;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestService {

  @Context
  HttpServletRequest req;

  @Inject
  ImageService iService;
  @Inject
  SightService fService;
  @Inject
  OrderService oService;
  @Inject
  UserService uService;

  Logger logger = Logger.getLogger(RestService.class.getName());

  @GET
  @Path("/images/{name}")
  @Produces({MediaType.APPLICATION_JSON, "image/png", "image/jpg"})
  public Response download(@PathParam("name") String name) {
    File file = iService.getImage(name);
    String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
    return Response.ok().entity(file).type("image/" + extension).build();
  }

  @GET
  @Path("/sights")
  public PagedCollection getList() {
    return search(new SightsPagedCollectionConfig());
  }

  @GET
  @Path("/sights/import")
  public void runImporter() {
    fService.runImporter();
  }

  @POST
  @Path("/sights/search")
  public PagedCollection search(SightsPagedCollectionConfig config) {
    PagedEntityCollection<Sight> plist = fService.getList(config);
    return new PagedCollection(
        plist.items.stream().map(f -> new SightOnListingRO(f)).collect(Collectors.toList()),
        plist.config);
  }

  @GET
  @Path("/sights/{id}")
  public SightRO get(@PathParam("id") Long id) {
    return new SightRO(fService.get(id));
  }

  @POST
  @Path("/orders")
  public OrderORO create(OrderIRO iro) {
    Collection<Triplet<Long, Date, Integer>> tickets = iro.entries.stream()
        .map(e -> new Triplet<>(e.id, e.date, e.quantity)).collect(Collectors.toList());
    return new OrderORO(oService.create(tickets, iro.details));
  }

  @GET
  @Path("/tickets")
  public List<OrderSightDateEntryOnListingORO> tickets() {
    return oService.getOrderSightDateEntries().stream().map(OrderSightDateEntryOnListingORO::new)
        .collect(Collectors.toList());
  }

  @GET
  @Path("/tickets/{id}")
  public OrderSightDateEntryORO ticket(@PathParam("id") Long id) {
    return new OrderSightDateEntryORO(oService.getOrderSightDateEntry(id));
  }

  @DELETE
  @Path("/tickets/{id}")
  public void deleteTicket(@PathParam("id") Long id) {
    oService.deleteOrderSightDateEntry(id);
  }

  @GET
  @Path("/users/me")
  public UserORO me() {
    return new UserORO(uService.me());
  }

  @POST
  @Path("/login/socialMedia")
  public Response loginBySocialMedia(LoginIRO iro) {
    String code = iro.token == null ? iro.idToken : iro.token;
    boolean success = login("", code);
    if (!success) {
      return Response.status(Status.UNAUTHORIZED).build();
    }
    return Response.ok().build();
  }

  @GET
  @Path("/logout")
  public void logout() {
    try {
      req.logout();
    } catch (ServletException e) {
    }
    req.getSession().invalidate();
  }

  private boolean login(String string, String password) {
    try {
      req.login("", password);
      return true;
    } catch (ServletException e) {
      logger.warning("Failed to log in: " + e.getMessage());
      return false;
    }
  }

}
