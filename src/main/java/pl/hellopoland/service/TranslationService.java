package pl.hellopoland.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;
import pl.hellopoland.bo.ModelSuperclass;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.Translation;
import pl.hellopoland.dto.SightDTO;

@LocalBean
@Stateless
public class TranslationService extends ServiceSuperclass {

  public List<Translation> createSightLanguageVersion(Sight bo, SightDTO dto, String language) {
    var translation = new Translation();
    translation.putLanguage(language);
    translation.generateKey(bo, "name");
    translation.setValue(dto.name);
    em.persist(translation);
    em.flush();
    translation = new Translation();
    translation.putLanguage(language);
    translation.generateKey(bo, "description");
    translation.setValue(dto.description);
    em.persist(translation);
    em.flush();
    translation = new Translation();
    translation.putLanguage(language);
    translation.generateKey(bo, "lead");
    translation.setValue(dto.lead);
    em.persist(translation);
    em.flush();
    return getTranslations(bo, language);
  }

  private List<Translation> getTranslations(ModelSuperclass bo, String language) {
    return em.createQuery("from Translation t where t.key like :key", Translation.class)
        .setParameter("key", bo.getClass().getSimpleName() + Translation.KEY_DELIMITER + bo.getId()
            + Translation.KEY_DELIMITER + "%")
        .getResultList();
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

}
