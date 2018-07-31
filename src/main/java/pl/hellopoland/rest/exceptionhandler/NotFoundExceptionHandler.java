package pl.hellopoland.rest.exceptionhandler;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.hellopoland.exception.notfound.NotFoundBaseException;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundBaseException> {

  @Override
  public Response toResponse(NotFoundBaseException e) {
    return Response.status(NOT_FOUND)
        .entity(Json.createObjectBuilder().add("message", e.getMessage()).build()).build();
  }
}
