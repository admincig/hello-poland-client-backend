package pl.hellopoland;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ConflictingException extends RuntimeException {
  private static final long serialVersionUID = -2347288019537878183L;

  public ConflictingException(String message) {
    super(message);
  }

  public ConflictingException(String message, Throwable cause) {
    super(message, cause);
  }
}
