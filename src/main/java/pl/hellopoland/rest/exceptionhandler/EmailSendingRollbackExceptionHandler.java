package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.email.EmailSendingRollbackException;
import pl.hellopoland.rest.dto.AbstractJSONError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class EmailSendingRollbackExceptionHandler
    implements ExceptionMapper<EmailSendingRollbackException> {

  @Override
  public Response toResponse(EmailSendingRollbackException e) {
    return Response.status(CONFLICT)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null)).build();
  }
}
