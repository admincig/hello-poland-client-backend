package pl.hellopoland.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.bo.ModelSuperclass;
import pl.hellopoland.bo.Translation;
import pl.hellopoland.dto.DTOSuperclass;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;

@LocalBean
@Stateless
public class TranslationService extends ServiceSuperclass {

  /**
   * The list of the names of the fields excluded from translation.
   */
  private static final List<String> EXCLUDED_FIELDS_NAMES =
      List.of("Sight.email", "Sight.phone", "Sight.defaultLanguage", "SightEvent.email",
          "SightEvent.phone", "SightEvent.defaultLanguage", "Agreement.linkUrl");

  public <T extends ModelSuperclass, D extends DTOSuperclass> T createEntityLanguageVersion(T bo,
      D dto, LanguageVersion language) {

    Predicate<? super Field> predicate = f -> (f.getType().equals(String.class)
        && !EXCLUDED_FIELDS_NAMES.contains(bo.getClass().getSimpleName() + "." + f.getName()));

    List<Field> dtoStringFields = Arrays.asList(dto.getClass().getFields()).stream()
        .filter(predicate).collect(Collectors.toList());

    for (Field field : dtoStringFields) {
      var translation = new Translation();
      translation.setLanguage(language);
      translation.generateKey(bo, field.getName());
      try {
        bo.getClass().getDeclaredField(field.getName());
        translation.setValue((String) field.get(dto));
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
        continue;
      }
      try {
        em.persist(translation);
        em.flush();
      } catch (Exception e) {
        throw new ConflictingException(
            "Can not create a new language version because it already exists");
      }
    }
    return translateEntity(bo, language.getLanuage(), true);
  }

  public <T extends ModelSuperclass, D extends DTOSuperclass> T updateEntityLanguageVersion(T bo,
      D dto, String language) {
    var translations = getTranslations(bo, language);
    for (Translation t : translations) {
      var key = t.getKey();
      var fieldName = key.substring(key.lastIndexOf(Translation.KEY_DELIMITER) + 1);
      try {
        var value = (String) dto.getClass().getField(StringUtils.uncapitalize(fieldName)).get(dto);
        if (value != null) {
          t.setValue(value);
        }
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
          | SecurityException e) {
        continue;
      }
    }
    return translateEntity(bo, language, true);
  }

  public <T extends ModelSuperclass> T translateEntity(T bo, String language,
      boolean fetchColections) {
    if (fetchColections) {
      fetchColections(bo);
    }
    var translations = getTranslations(bo, getLanguageSymbol(language));
    em.detach(bo);
    for (Translation translation : translations) {
      if (StringUtils.isNotBlank(translation.getValue())) {
        var key = translation.getKey();
        var fieldName = key.substring(key.lastIndexOf(Translation.KEY_DELIMITER) + 1);
        try {
          bo.getClass().getMethod("set" + StringUtils.capitalize(fieldName), String.class)
              .invoke(bo, translation.getValue());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
            | NoSuchMethodException | SecurityException e) {
          continue;
        }
      }
    }
    return bo;
  }

  public <T extends ModelSuperclass> List<T> translateEntities(Collection<T> bos, String language,
      boolean fetchColections) {
    return bos.stream().map(bo -> {
      bo = translateEntity(bo, language, fetchColections);
      return bo;
    }).collect(Collectors.toList());
  }

  private <T extends ModelSuperclass> void fetchColections(T bo) {
    Predicate<? super Field> predicateNotEmptyCollection = field -> {
      try {
        return Collection.class.isAssignableFrom(field.getType())
            && bo.getClass().getMethod("get" + StringUtils.capitalize(field.getName()))
                .invoke(bo) != null
            && !((Collection<?>) bo.getClass()
                .getMethod("get" + StringUtils.capitalize(field.getName())).invoke(bo)).isEmpty();
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
          | NoSuchMethodException | SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return false;
    };

    Arrays.asList(bo.getClass().getDeclaredFields()).stream().filter(predicateNotEmptyCollection)
        .map(field -> {
          try {
            return (Collection<?>) bo.getClass()
                .getMethod("get" + StringUtils.capitalize(field.getName())).invoke(bo);
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
              | NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return null;
        }).forEach(collection -> collection.size());
  }

  private List<Translation> getTranslations(ModelSuperclass bo, String language) {
    LanguageVersion lang;
    try {
      lang = LanguageVersion.valueOf(language.toUpperCase());
    } catch (IllegalArgumentException e) {
      lang = LanguageVersion.EN_GB;
    }
    return em
        .createQuery("from Translation t where t.key like :key and language = :language",
            Translation.class)
        .setParameter("key", bo.getClass().getSimpleName() + Translation.KEY_DELIMITER + bo.getId()
            + Translation.KEY_DELIMITER + "%")
        .setParameter("language", lang).getResultList();
  }

  private String getLanguageSymbol(String language) {
    int indexOfDelimiter = language.indexOf("-");
    return language.substring(0, indexOfDelimiter == -1 ? language.length() : indexOfDelimiter);
  }

}
