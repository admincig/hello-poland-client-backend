package pl.hellopoland.rest.exceptionhandler;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.hellopoland.exception.notfound.NotFoundBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundBaseException> {

  @Override
  public Response toResponse(NotFoundBaseException e) {
    return Response.status(NOT_FOUND)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null))
        .build();
  }
}
