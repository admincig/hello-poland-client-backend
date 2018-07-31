package pl.hellopoland.rest.dto;

import java.io.Serializable;

public class AbstractJSONError implements Serializable {

  private static final long serialVersionUID = 2681281975330282406L;

  public String exception;
  public String message;
  public Object object;

  public AbstractJSONError() {

  }

  public AbstractJSONError(Class<? extends Exception> exception, String message, Object object) {
    this.exception = exception.getSimpleName();
    this.message = message;
    this.object = object;
  }
}
