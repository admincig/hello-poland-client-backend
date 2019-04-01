package pl.hellopoland.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class BeanUtils {

  public static <T> T copyNotNullProperties(T source, T target) {
    var targetFields = target.getClass().getDeclaredFields();
    var sourceFields = Stream.of(source.getClass().getDeclaredFields())
        .filter(field -> valueIsNotNull(source, field)).collect(Collectors.toSet());

    for (Field tField : targetFields) {
      for (Field sField : sourceFields) {
        if (tField.getType().equals(sField.getType())
            && tField.getName().equals(sField.getName())) {
          try {
            copyProperty(source, target, sField);
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
              | NoSuchMethodException | SecurityException e) {
            // do nothing
            // e.printStackTrace();
          }
          break;
        }
      }
    }

    return target;
  }

  private static <T> boolean valueIsNotNull(T object, Field field) {
    try {
      return getFieldValue(object, field) != null;
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      return false;
    }
  }

  private static <T> Object getFieldValue(T object, Field field)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    return object.getClass().getMethod("get" + StringUtils.capitalize(field.getName()))
        .invoke(object);
  }

  private static <T> void copyProperty(T source, T target, Field field)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoSuchMethodException, SecurityException {
    target.getClass().getMethod("set" + StringUtils.capitalize(field.getName()), field.getType())
        .invoke(target, getFieldValue(source, field));
  }

}
