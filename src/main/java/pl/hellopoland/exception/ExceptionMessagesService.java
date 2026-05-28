package pl.hellopoland.exception;

import jakarta.enterprise.context.RequestScoped;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@RequestScoped
public class ExceptionMessagesService {

  private static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
  private static final String UNKNOWN_EXTERNAL_ERROR = "UNKNOWN_EXTERNAL_ERROR";
  private static final String EXTERNAL_CODE_PREFIX = "external.code.";
  private ResourceBundle resourceBundle;

  public ExceptionMessagesService() {
    resourceBundle = ResourceBundle.getBundle("i18n/messages", new Locale("pl"),
        new Utf8ResourceBundleControl());
  }

  public String getMessage(String exceptionSimpleClassName) {
    try {
      if (resourceBundle.containsKey(exceptionSimpleClassName)) {
        return resourceBundle.getString(exceptionSimpleClassName);
      }
    } catch (MissingResourceException ignored) {
    }
    return getFallbackMessage(UNKNOWN_ERROR, "Nieznany błąd.");
  }

  public String getMessageByCode(String code) {
    if (code == null || code.isBlank()) {
      return getFallbackMessage(UNKNOWN_EXTERNAL_ERROR,
          "Nie udało się wykonać operacji w zewnętrznym systemie.");
    }

    String key = EXTERNAL_CODE_PREFIX + code;
    try {
      if (resourceBundle.containsKey(key)) {
        return resourceBundle.getString(key);
      }
    } catch (MissingResourceException ignored) {
    }
    return getFallbackMessage(UNKNOWN_EXTERNAL_ERROR,
        "Nie udało się wykonać operacji w zewnętrznym systemie.");
  }

  private String getFallbackMessage(String key, String fallback) {
    try {
      if (resourceBundle.containsKey(key)) {
        return resourceBundle.getString(key);
      }
    } catch (MissingResourceException ignored) {
    }
    return fallback;
  }
}
