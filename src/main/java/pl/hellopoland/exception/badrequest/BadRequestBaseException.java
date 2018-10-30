package pl.hellopoland.exception.badrequest;

import pl.hellopoland.exception.BaseException;

public abstract class BadRequestBaseException extends BaseException {
  private static final long serialVersionUID = 67063574379357273L;

  public BadRequestBaseException() {}

  public BadRequestBaseException(String message) {
    super(message);
  }

}
