package pl.hellopoland.rest.dto;

import java.io.Serializable;

public class AbstractJSONError implements Serializable {

  private static final long serialVersionUID = 2681281975330282406L;

  public String exception;
  public String message;
  public Object object;
  public String code;

  public AbstractJSONError(Class<? extends Exception> exception, String message, Object object) {
    this(exception, message, object, null);
  }

  public AbstractJSONError(Class<? extends Exception> exception, String message, Object object,
      String code) {
    this.exception = exception.getSimpleName();
    this.message = message;
    this.object = object;
    this.code = code;
  }
}
