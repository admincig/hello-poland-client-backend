package pl.hellopoland.rest;

import pl.hellopoland.annotation.DateFormat;
import pl.hellopoland.annotation.DateTimeFormat;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

@Provider
public class DateParameterConverterProvider implements ParamConverterProvider {

  @SuppressWarnings("unchecked")
  @Override
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType,
      Annotation[] annotations) {
    if (Date.class.equals(rawType)) {
      final DateParameterConverter dateParameterConverter = new DateParameterConverter();

      for (Annotation annotation : annotations) {
        if (DateTimeFormat.class.equals(annotation.annotationType())) {
          dateParameterConverter.setCustomDateTimeFormat((DateTimeFormat) annotation);
        } else if (DateFormat.class.equals(annotation.annotationType())) {
          dateParameterConverter.setCustomDateFormat((DateFormat) annotation);
        }
      }
      return (ParamConverter<T>) dateParameterConverter;
    }
    return null;
  }

}
