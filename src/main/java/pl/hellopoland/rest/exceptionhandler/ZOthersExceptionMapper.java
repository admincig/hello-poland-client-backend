package pl.hellopoland.rest.exceptionhandler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class ZOthersExceptionMapper implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception exception) {
    Logger.getLogger(ZOthersExceptionMapper.class.getName()).warning(exception.getMessage());
    return Response.status(500).entity(exception.getMessage()).build();
  }

}
