package pl.hellopoland.exception;

public abstract class BaseException extends RuntimeException {
  private static final long serialVersionUID = -124530655538448160L;

  private String code;

  public BaseException() {}

  public BaseException(String message) {
    super(message);
  }

  public BaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseException(String message, String code) {
    super(message);
    this.code = code;
  }

  public BaseException(String message, String code, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
