package pl.hellopoland.exception.badrequest;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BadRequestException extends BadRequestBaseException {
  private static final long serialVersionUID = -620784240239165262L;

  public BadRequestException(String message) {
    super(message);
  }

}
