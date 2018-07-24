package pl.hellopoland.rest.exceptionhandler;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.hellopoland.exception.badrequest.BadRequestBaseException;
import pl.hellopoland.rest.dto.AbstractJSONError;

@Provider
public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestBaseException> {

  @Override
  public Response toResponse(BadRequestBaseException e) {
    return Response.status(BAD_REQUEST)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null)).build();
  }
}
