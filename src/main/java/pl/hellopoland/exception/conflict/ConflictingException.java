package pl.hellopoland.exception.conflict;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ConflictingException extends ConflictBaseException {

  private static final long serialVersionUID = -2347288019537878183L;

  public ConflictingException(String message) {
    super(message);
  }

  public ConflictingException(String message, String code) {
    super(message, code);
  }

  public ConflictingException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConflictingException(String message, String code, Throwable cause) {
    super(message, code, cause);
  }
}
