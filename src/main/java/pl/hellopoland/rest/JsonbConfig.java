package pl.hellopoland.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonbConfig implements ContextResolver<Jsonb> {

  public static final String DATE_TIME_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssxxx";
  public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_FORMAT_STRING);

  private static Jsonb instance;

  public static Jsonb getInstance() {
    if (instance == null) {
      var config = new javax.json.bind.JsonbConfig().withDateFormat(DATE_TIME_FORMAT_STRING, null);
      instance = JsonbBuilder.create(config);
    }
    return instance;
  }

  @Override
  public Jsonb getContext(Class<?> type) {
    return getInstance();
  }

}
