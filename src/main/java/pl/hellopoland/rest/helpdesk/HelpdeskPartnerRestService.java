package pl.hellopoland.rest.helpdesk;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.service.api.helpdesk.ServiceHelpdeskAPI;

@RequestScoped
@Path("/helpdesk/partners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelpdeskPartnerRestService {

  @Inject
  private ServiceHelpdeskAPI service;

  @POST
  public PartnerDTO add(PartnerDTO partner) {
    return service.addPartner(partner);
  }

  @GET
  public Response listPartners() {
    var config = new PartnerPagedCollectionConfig();
    return Response.ok(service.listPartners(config)).build();
  }
}
