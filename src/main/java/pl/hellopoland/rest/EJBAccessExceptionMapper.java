package pl.hellopoland.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBAccessException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EJBAccessExceptionMapper implements ExceptionMapper<EJBAccessException> {

  private Logger logger = Logger.getLogger(EJBAccessExceptionMapper.class.getName());
  @Context
  private HttpServletRequest request;

  @Override
  public Response toResponse(EJBAccessException e) {
    logger.log(Level.WARNING, e.getMessage());

    Status status = null;
    if (request.getUserPrincipal() == null) {
      status = Status.UNAUTHORIZED;
    } else {
      status = Status.FORBIDDEN;
    }
    return Response.status(status).build();
  }

}
