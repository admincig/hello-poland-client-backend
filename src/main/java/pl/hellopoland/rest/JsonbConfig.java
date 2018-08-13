package pl.hellopoland.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonbConfig implements ContextResolver<Jsonb> {

  public static final String SIMPLE_DATE_TIME_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
  public static final DateFormat SIMPLE_DATE_TIME_FORMAT =
      new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT_STRING);
  public static final String EXTENDED_DATE_TIME_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSxxx";

  private static Jsonb instance;

  public static Jsonb getInstance() {
    if (instance == null) {
      var config =
          new javax.json.bind.JsonbConfig().withDateFormat(EXTENDED_DATE_TIME_FORMAT_STRING, null);
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
