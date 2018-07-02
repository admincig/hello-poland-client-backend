package pl.hellopoland.exception.conflict;

import pl.hellopoland.exception.BaseException;

public abstract class ConflictBaseException extends BaseException {

  public ConflictBaseException() {

  }

  public ConflictBaseException(String message) {
    super(message);
  }
}
