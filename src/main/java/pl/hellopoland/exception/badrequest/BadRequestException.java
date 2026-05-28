package pl.hellopoland.exception.badrequest;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BadRequestException extends BadRequestBaseException {
  private static final long serialVersionUID = -620784240239165262L;

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, String code) {
    super(message, code);
  }

}
