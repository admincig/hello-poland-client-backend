package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.conflict.ConflictBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class ConflictExceptionHandler implements ExceptionMapper<ConflictBaseException> {

  @Override
  public Response toResponse(ConflictBaseException e) {
    return Response.status(CONFLICT)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null)).build();
  }
}
