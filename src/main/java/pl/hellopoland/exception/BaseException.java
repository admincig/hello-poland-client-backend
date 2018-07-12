package pl.hellopoland.exception;

public abstract class BaseException extends RuntimeException {
  private static final long serialVersionUID = -124530655538448160L;

  public BaseException() {
  }

  public BaseException(String message) {
    super(message);
  }
}
