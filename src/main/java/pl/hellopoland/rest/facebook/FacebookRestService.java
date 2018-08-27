package pl.hellopoland.rest.facebook;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.hellopoland.util.FacebookFanPagePostReader;

@Path("/fb")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacebookRestService {

  @GET
  @Path("/fanpage/posts")
  public Response getPosts() {
    return Response.ok(FacebookFanPagePostReader.getInstance().readPosts()).build();
  }
}
