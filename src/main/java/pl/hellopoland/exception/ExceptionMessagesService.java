package pl.hellopoland.exception;

import jakarta.enterprise.context.RequestScoped;
import java.util.Locale;
import java.util.ResourceBundle;

@RequestScoped
public class ExceptionMessagesService {

  private ResourceBundle resourceBundle;

  public ExceptionMessagesService() {
    resourceBundle = ResourceBundle.getBundle("i18n/messages", new Locale("pl"),
        new Utf8ResourceBundleControl());
  }

  public String getMessage(String exceptionSimpleClassName) {
    return resourceBundle.getString(exceptionSimpleClassName);
  }
}
