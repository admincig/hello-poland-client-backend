package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.UnauthorizedException;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionHandler implements ExceptionMapper<UnauthorizedException> {

  @Override
  public Response toResponse(UnauthorizedException e) {
    return Response.status(Status.UNAUTHORIZED)
        .entity(Json.createObjectBuilder().add("message", e.getMessage()).build()).build();
  }
}
