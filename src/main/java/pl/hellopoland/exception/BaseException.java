package pl.hellopoland.exception;

public abstract class BaseException extends RuntimeException {

  public String message;

  public BaseException() {

  }

  public BaseException(String message) {
    this.message = message;
  }
}
