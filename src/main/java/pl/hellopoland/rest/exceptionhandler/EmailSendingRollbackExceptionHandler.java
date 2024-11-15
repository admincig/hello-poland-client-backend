package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.email.EmailSendingRollbackException;
import pl.hellopoland.rest.dto.AbstractJSONError;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import static jakarta.ws.rs.core.Response.Status.CONFLICT;

@Provider
public class EmailSendingRollbackExceptionHandler
    implements ExceptionMapper<EmailSendingRollbackException> {

  @Override
  public Response toResponse(EmailSendingRollbackException e) {
    return Response.status(CONFLICT)
        .entity(new AbstractJSONError(e.getClass(), e.getMessage(), null)).build();
  }
}
