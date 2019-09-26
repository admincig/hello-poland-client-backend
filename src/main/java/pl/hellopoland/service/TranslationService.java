package pl.hellopoland.service;

import java.lang.System.Logger.Level;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.annotation.Multilingual;
import pl.hellopoland.bo.Translation;
import pl.hellopoland.dto.DTOSuperclass;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.Translated;

@LocalBean
@Stateless
public class TranslationService extends ServiceSuperclass {

  /**
   * Checks whether the entity object implements interface Translated.
   */
  @SuppressWarnings("unchecked")
  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    Translated param = null;
    Collection<Translated> collectionParam = null;
    for (int i = 0; i < ctx.getParameters().length; i++) {
      try {
        param = (Translated) ctx.getParameters()[i];
        break;
      } catch (ClassCastException e1) {
        try {
          collectionParam = (Collection<Translated>) ctx.getParameters()[i];
          break;
        } catch (ClassCastException e2) {
          continue;
        }
      }
    }
    if (param == null && collectionParam == null) {
      throw new ConflictingException("Entity does not implement interface Translated");
    }
    return ctx.proceed();
  }

  public <T extends Translated, D extends DTOSuperclass> T createEntityLanguageVersion(T bo,
      D dto, LanguageVersion language) {
    if (isTranslated(bo, language)) {
      removeTranslations(bo, language);
    }

    List<Field> dtoStringFields = Stream.of(dto.getClass().getFields())
        .filter(f -> f.getType().equals(String.class))
        .collect(Collectors.toList());

    for (Field field : dtoStringFields) {
      try {
        if (bo.getClass().getDeclaredField(field.getName())
            .isAnnotationPresent(Multilingual.class)) {
          var translation = new Translation();
          translation.setValue((String) field.get(dto));
          translation.setLanguage(language);
          translation.generateKey(bo, field.getName());
          em.persist(translation);
          em.flush();
        }
      } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
        logger.log(Level.DEBUG, "Failed to translate field " + field.getName(), e);
        continue;
      } catch (Exception e) {
        throw new ConflictingException(
            "Can not create a new language version because it already exists");
      }
    }
    addAvailableLanguageVersion(bo, language);
    return translateEntity(bo, language, true);
  }

  public <T extends Translated, D extends DTOSuperclass> T updateEntityLanguageVersion(T bo,
      D dto, LanguageVersion language) {
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

  public <T extends Translated> T translateEntity(T bo, LanguageVersion language,
      boolean fetchColections) {
    if (fetchColections) {
      fetchColections(bo);
    }
    var translations = getTranslations(bo, language);
    em.clear();
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
    bo.setCurrentLanguage(language);
    return bo;
  }

  public <T extends Translated> List<T> translateEntities(Collection<T> bos,
      LanguageVersion language, boolean fetchColections) {
    return bos.stream().map(bo -> bo = translateEntity(bo, language, fetchColections))
        .collect(Collectors.toList());
  }

  public <T extends Translated> void deleteEntityTranslations(T bo, LanguageVersion language) {
    try {
      if (((LanguageVersion) bo.getClass().getMethod("getDefaultLanguage").invoke(bo))
          .equals(language)) {
        throw new ConflictingException("Deleting default language version is forbidden.");
      }
      if ((boolean) bo.getClass().getMethod("deleteAvailableLanguageVersion", LanguageVersion.class)
          .invoke(bo, language)) {
        em.flush();
        getTranslations(bo, language).forEach(t -> t.setDeleted(true));
      } else {
        throw new ResourceNotFoundException();
      }
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
      throw new ConflictingException(e.getLocalizedMessage());
    }
  }

  private <T extends Translated> void removeTranslations(T bo, LanguageVersion language) {
    em.createQuery("delete from Translation t where t.key like :key and language = :language")
        .setParameter("key", getKey(bo)).setParameter("language", language).executeUpdate();
  }

  public <T extends Translated> boolean isTranslated(T bo, LanguageVersion language) {
    return em
        .createQuery("from Translation t where t.key like :key and language = :language",
            Translation.class)
        .setParameter("key", getKey(bo)).setParameter("language", language).setMaxResults(1)
        .getResultList().size() == 1;
  }

  private <T extends Translated> void fetchColections(T bo) {
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

  public List<Translation> getTranslations(Translated bo, LanguageVersion language) {
    return em
        .createQuery("from Translation t where t.key like :key and language = :language",
            Translation.class)
        .setParameter("key", getKey(bo)).setParameter("language", language).getResultList();
  }

  private <T extends Translated> void addAvailableLanguageVersion(T bo,
      LanguageVersion language) {
    try {
      bo.getClass().getMethod("addAvailableLanguageVersion", LanguageVersion.class).invoke(bo,
          language);
      em.flush();
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      logger.log(Level.ERROR, "failed to invoke reflection method", e);
    }
  }

  private String getKey(Translated bo) {
    return bo.getClass().getSimpleName() + Translation.KEY_DELIMITER + bo.getId()
        + Translation.KEY_DELIMITER + "%";
  }

}
