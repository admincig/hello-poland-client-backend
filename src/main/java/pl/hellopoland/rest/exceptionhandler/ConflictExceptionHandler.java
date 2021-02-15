package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.conflict.ConflictBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class ConflictExceptionHandler implements ExceptionMapper<ConflictBaseException> {

  @Override
  public Response toResponse(ConflictBaseException e) {
    return Response.status(CONFLICT)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null)).build();
  }
}
