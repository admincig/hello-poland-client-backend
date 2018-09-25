package pl.hellopoland.rest;

import java.util.Locale;
import java.util.logging.Logger;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonbConfig implements ContextResolver<Jsonb> {

  public static final String EXTENDED_DATE_TIME_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm";

  private static Jsonb instance;

  public static Jsonb getInstance() {
    if (instance == null) {
      var config = new javax.json.bind.JsonbConfig()
          .withDateFormat(EXTENDED_DATE_TIME_FORMAT_STRING, new Locale("en", "GB"))
          .withAdapters(new LocalTimeCustomAdapter());
      config.setProperty("jsonb.fail-on-unknown-properties", false);
      Logger.getLogger("JsonbConfig").info(config.getAsMap().toString());
      instance = JsonbBuilder.create(config);
    }
    return instance;
  }

  @Override
  public Jsonb getContext(Class<?> type) {
    return getInstance();
  }

}
