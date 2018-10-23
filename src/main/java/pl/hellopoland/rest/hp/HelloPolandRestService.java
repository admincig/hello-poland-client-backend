package pl.hellopoland.rest.hp;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.PartnerDTO;

@RequestScoped
@Path("/hp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloPolandRestService {

  @Path("/partner")
  public PartnerDTO add() {

  }

}
