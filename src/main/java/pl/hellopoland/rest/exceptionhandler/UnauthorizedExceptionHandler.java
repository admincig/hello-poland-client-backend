package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.UnauthorizedException;

import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionHandler implements ExceptionMapper<UnauthorizedException> {

  @Override
  public Response toResponse(UnauthorizedException e) {
    return Response.status(Status.UNAUTHORIZED)
        .entity(Json.createObjectBuilder().add("message", e.getMessage()).build()).build();
  }
}
