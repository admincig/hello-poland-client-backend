package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.badrequest.BadRequestBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestBaseException> {

  @Override
  public Response toResponse(BadRequestBaseException e) {
    return Response.status(BAD_REQUEST)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null)).build();
  }
}
