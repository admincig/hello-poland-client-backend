package pl.hellopoland.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.hellopoland.ConflictingException;

@Provider
public class ConflictingExceptionMapper implements ExceptionMapper<ConflictingException> {

  private Logger logger = Logger.getLogger(ConflictingExceptionMapper.class.getName());

  @Override
  public Response toResponse(ConflictingException e) {
    logger.log(Level.WARNING, "", e);
    JsonObject body = Json.createObjectBuilder().add("message", e.getLocalizedMessage()).build();
    return Response.status(Status.CONFLICT).entity(body).build();
  }

}
