package pl.hellopoland.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ParamConverter;
import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.annotation.DateTimeFormat;

public class DateParameterConverter implements ParamConverter<Date> {

  public static final String DEFAULT_FORMAT = DateTimeFormat.DEFAULT_DATE_TIME_FORMAT;
  private DateTimeFormat customDateTimeFormat;
  private DateFormat customDateFormat;

  @Override
  public Date fromString(String value) {
    String format = DEFAULT_FORMAT;
    if (customDateFormat != null) {
      format = customDateFormat.value();
    } else if (customDateTimeFormat != null) {
      format = customDateTimeFormat.value();
    }
    try {
      return new SimpleDateFormat(format).parse(value);
    } catch (ParseException ex) {
      throw new WebApplicationException(ex);
    }
  }

  @Override
  public String toString(Date value) {
    return new SimpleDateFormat(DEFAULT_FORMAT).format(value);
  }

  public void setCustomDateTimeFormat(DateTimeFormat customDateTimeFormat) {
    this.customDateTimeFormat = customDateTimeFormat;
  }

  public void setCustomDateFormat(DateFormat customDateFormat) {
    this.customDateFormat = customDateFormat;
  }

}
