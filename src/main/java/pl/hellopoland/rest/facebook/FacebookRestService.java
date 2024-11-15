package pl.hellopoland.rest.facebook;

import pl.hellopoland.util.FacebookFanPagePostReader;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/market/news")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacebookRestService {

  @GET
  @Path("/fanpage/posts")
  public Response getPosts() {
    return Response.ok(FacebookFanPagePostReader.getInstance().readPosts()).build();
  }
}
