package pl.hellopoland.rest.hp;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.service.api.hp.HellopolandServiceAPI;

@RequestScoped
@Path("/hp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloPolandRestService {
  @Inject
  private HellopolandServiceAPI service;

  @POST
  @Path("/partners")
  public PartnerDTO add(PartnerDTO partner) {
    return service.addPartner(partner);
  }

}
