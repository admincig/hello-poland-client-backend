package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.preconditionfailed.PreconditionFailedBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.PRECONDITION_FAILED;

@Provider
public class PreconditionFailedExceptionHandler
    implements ExceptionMapper<PreconditionFailedBaseException> {

  @Override
  public Response toResponse(PreconditionFailedBaseException e) {
    return Response.status(PRECONDITION_FAILED)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null)).build();
  }
}
