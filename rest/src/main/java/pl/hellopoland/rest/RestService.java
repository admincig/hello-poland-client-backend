package pl.hellopoland.rest;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.order.OrderService;
import pl.hellopoland.rest.dto.OrderIRO;
import pl.hellopoland.rest.dto.OrderORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.SightOnListingRO;
import pl.hellopoland.rest.dto.SightRO;
import pl.hellopoland.sight.Sight;
import pl.hellopoland.sight.SightService;
import pl.hellopoland.util.PagedEntityCollection;
import pl.hellopoland.util.Triplet;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class RestService {

  @Inject
  ImageService iService;
  @Inject
  SightService fService;
  @Inject
  OrderService oService;

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

}
