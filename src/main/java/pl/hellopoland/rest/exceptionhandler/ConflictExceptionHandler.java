package pl.hellopoland.rest.exceptionhandler;

import static javax.ws.rs.core.Response.Status.CONFLICT;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.hellopoland.exception.conflict.ConflictBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

@Provider
public class ConflictExceptionHandler implements ExceptionMapper<ConflictBaseException> {

  @Override
  public Response toResponse(ConflictBaseException e) {
    return Response.status(CONFLICT)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null))
        .build();
  }
}
