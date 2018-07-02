package pl.hellopoland.rest.exceptionhandler;

import static javax.ws.rs.core.Response.Status.PRECONDITION_FAILED;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.hellopoland.exception.preconditionfailed.PreconditionFailedBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

@Provider
public class PreconditionFailedExceptionHandler implements
    ExceptionMapper<PreconditionFailedBaseException> {

  @Override
  public Response toResponse(PreconditionFailedBaseException e) {
    return Response.status(PRECONDITION_FAILED)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null))
        .build();
  }
}
