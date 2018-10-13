package pl.hellopoland.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.bo.ModelSuperclass;
import pl.hellopoland.bo.Translation;
import pl.hellopoland.bo.Translation.LanguageVersion;
import pl.hellopoland.dto.DTOSuperclass;

@LocalBean
@Stateless
public class TranslationService extends ServiceSuperclass {

  /**
   * The list of the names of the fields excluded from translation.
   */
  private static final List<String> EXCLUDED_FIELDS_NAMES =
      List.of("Sight.email", "Sight.phone");

  public <T extends ModelSuperclass, D extends DTOSuperclass> List<Translation> createEntityLanguageVersion(
      T bo, D dto, String language) {

    Predicate<? super Field> predicate = f -> (f.getType().equals(String.class)
        && !EXCLUDED_FIELDS_NAMES.contains(bo.getClass().getSimpleName() + "." + f.getName()));

    List<Field> dtoStringFields = Arrays.asList(dto.getClass().getFields()).stream()
        .filter(predicate).collect(Collectors.toList());
    var translations = new ArrayList<Translation>();
    for (Field field : dtoStringFields) {
      var translation = new Translation();
      translation.putLanguage(language);
      translation.generateKey(bo, field.getName());
      try {
        translation.setValue((String) field.get(dto));
      } catch (IllegalArgumentException | IllegalAccessException e) {
        continue;
      }
      em.persist(translation);
      em.flush();
      translations.add(translation);
    }
    return translations;
  }

  public <T extends ModelSuperclass> T translateEntity(T bo, List<Translation> translations) {
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

  public <T extends ModelSuperclass, D extends DTOSuperclass> List<Translation> updateTranslations(
      T bo, D dto, String language) {
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
    return translations;
  }

  private List<Translation> getTranslations(ModelSuperclass bo, String language) {
    return em
        .createQuery("from Translation t where t.key like :key and language = :language",
            Translation.class)
        .setParameter("key",
            bo.getClass().getSimpleName() + Translation.KEY_DELIMITER + bo.getId()
                + Translation.KEY_DELIMITER + "%")
        .setParameter("language", LanguageVersion.valueOf(language.toUpperCase())).getResultList();
  }

}
