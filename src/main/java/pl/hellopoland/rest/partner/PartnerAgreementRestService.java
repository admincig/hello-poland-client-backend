package pl.hellopoland.rest.partner;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.service.api.partner.AgreementServicePartnerAPI;

@Path("/partner/agreements")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartnerAgreementRestService {

  @Inject
  private AgreementServicePartnerAPI service;

  @GET
  public List<AgreementDTO> getForPartner() {
    return service.getForPartner();
  }

  @GET
  @Path("/{id}")
  public AgreementDTO get(@PathParam("id") Long id) {
    return service.getForPartner(id);
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    service.delete(id);
  }

  @PUT
  @Path("/{id}")
  public AgreementDTO update(@PathParam("id") Long id, AgreementDTO dto,
      @QueryParam("language") String language) {
    dto.id = id;
    return service.update(dto);
  }

}
