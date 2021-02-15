package pl.hellopoland.rest.market;

import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserInfoDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.api.market.UserServiceMarketAPI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
  public Response register(UserInfoDTO dto) {
    service.register(dto);
    return Response.ok().build();
  }

  @PATCH
  @Path("/me")
  public UserORO update(UserInfoDTO dto) {
    return service.updateUserDetails(dto);
  }

  @PATCH
  @Path("/me/agreements")
  public UserORO updateAgreements(UserInfoDTO dto) {
    return service.updateAgreements(dto);
  }

  @PATCH
  @Path("/me/password")
  public Response updatePassword(UserAuthDTO userAuthDTO) {
    service.updatePassword(userAuthDTO);
    return Response.ok().build();
  }

}
