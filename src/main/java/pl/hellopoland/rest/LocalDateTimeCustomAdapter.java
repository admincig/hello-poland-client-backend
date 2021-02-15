package pl.hellopoland.rest;

import javax.json.bind.adapter.JsonbAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeCustomAdapter implements JsonbAdapter<LocalDateTime, String> {

  private final static DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  @Override
  public String adaptToJson(LocalDateTime obj) throws Exception {
    return DATE_TIME_FORMAT.format(obj);
  }

  @Override
  public LocalDateTime adaptFromJson(String obj) throws Exception {
    return LocalDateTime.parse(obj, DATE_TIME_FORMAT);
  }

}
