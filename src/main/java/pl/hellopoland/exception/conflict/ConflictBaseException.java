package pl.hellopoland.exception.conflict;

import pl.hellopoland.exception.BaseException;

public abstract class ConflictBaseException extends BaseException {
  private static final long serialVersionUID = -7087482910697093259L;

  public ConflictBaseException() {

  }

  public ConflictBaseException(String message) {
    super(message);
  }

  public ConflictBaseException(String message, String code) {
    super(message, code);
  }

  public ConflictBaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConflictBaseException(String message, String code, Throwable cause) {
    super(message, code, cause);
  }
}
