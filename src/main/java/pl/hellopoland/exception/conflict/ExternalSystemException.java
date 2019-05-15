package pl.hellopoland.exception.conflict;

public class ExternalSystemException extends ConflictBaseException {
  private static final long serialVersionUID = 2430160301307820438L;

  public ExternalSystemException(String message) {
    super(message);
  }
}
