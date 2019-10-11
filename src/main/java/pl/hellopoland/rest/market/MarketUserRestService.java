package pl.hellopoland.rest.market;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.hellopoland.dto.UserInfoDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.market.UserServiceMarketAPI;

@Path("/market/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarketUserRestService {

  @Inject
  UserServiceMarketAPI service;

  @GET
  @Path("/me")
  public UserORO me() {
    return service.me();
  }

  @POST
  @Path("/register")
  public UserORO register(UserInfoDTO dto) {
    return service.register(dto);
  }

  @POST
  @Path("/update")
  public UserORO udpate(UserInfoDTO dto) {
    return service.update(dto);
  }
}
