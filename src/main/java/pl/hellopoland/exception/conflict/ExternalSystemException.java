package pl.hellopoland.exception.conflict;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ExternalSystemException extends ConflictBaseException {
  private static final long serialVersionUID = 2430160301307820438L;
  private final Integer statusCode;

  public ExternalSystemException(String message) {
    super(message);
    this.statusCode = null;
  }

  public ExternalSystemException(String message, String code, Integer statusCode) {
    super(message, code);
    this.statusCode = statusCode;
  }

  public ExternalSystemException(String message, String code, Integer statusCode, Throwable cause) {
    super(message, code, cause);
    this.statusCode = statusCode;
  }

  public Integer getStatusCode() {
    return statusCode;
  }
}
