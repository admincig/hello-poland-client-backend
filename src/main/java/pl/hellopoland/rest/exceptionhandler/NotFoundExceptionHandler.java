package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.notfound.NotFoundBaseException;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundBaseException> {

  @Override
  public Response toResponse(NotFoundBaseException e) {
    return Response.status(NOT_FOUND)
        .entity(Json.createObjectBuilder().add("message", e.getMessage()).build()).build();
  }
}
