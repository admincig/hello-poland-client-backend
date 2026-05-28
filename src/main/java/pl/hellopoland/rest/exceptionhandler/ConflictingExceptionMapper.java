package pl.hellopoland.rest.exceptionhandler;

import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.AbstractJSONError;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ConflictingExceptionMapper implements ExceptionMapper<ConflictingException> {

  private Logger logger = Logger.getLogger(ConflictingExceptionMapper.class.getName());

  @Override
  public Response toResponse(ConflictingException e) {
    logger.log(Level.WARNING, "", e);
    return Response.status(Status.CONFLICT)
        .entity(new AbstractJSONError(e.getClass(), e.getLocalizedMessage(), null, e.getCode()))
        .build();
  }

}
