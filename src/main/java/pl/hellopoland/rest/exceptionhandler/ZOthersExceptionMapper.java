package pl.hellopoland.rest.exceptionhandler;

import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ZOthersExceptionMapper implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception exception) {
    Logger.getLogger(ZOthersExceptionMapper.class.getName()).warning(exception.getMessage());
    return Response.status(500).entity(exception.getMessage()).build();
  }

}
