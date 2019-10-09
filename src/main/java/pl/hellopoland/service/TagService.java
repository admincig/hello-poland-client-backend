package pl.hellopoland.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.TagPagedCollectionConfig;
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.BeanUtils;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class TagService extends ServiceSuperclass {

  @Inject
  private TranslationService tService;

  public Tag create(TagDTO dto) {
    Tag tag = new Tag();
    tag.setLabel(dto.label);
    tag.setIconUrl(dto.iconUrl);
    tag.setRestricted(Boolean.TRUE.equals(dto.restricted));
    tag.setRecommended(Boolean.TRUE.equals(dto.recommended));
    tag.setDefaultLanguage(LanguageVersion.getForCreateAndUpdateEntity(dto.language));
    tag.setAvailableLanguageVersions(new HashSet<>(Set.of(tag.getDefaultLanguage())));
    em.persist(tag);
    tService.createEntityLanguageVersion(tag, dto, tag.getDefaultLanguage());
    return tag;
  }

  public Tag createLanguageVesrion(TagDTO dto, LanguageVersion language) {
    return tService.createEntityLanguageVersion(get(dto.id), dto, language);
  }

  public Tag get(long id) {
    return Optional.ofNullable(em.find(Tag.class, id))
        .orElseThrow(() -> new ResourceNotFoundException());
  }

  public PagedEntityCollection<Tag> pagedList(TagPagedCollectionConfig config) {
    List<Tag> list = getQuery(config).getResultList();
    return new PagedEntityCollection<>(list, config);
  }

  public Tag update(TagDTO dto, LanguageVersion lang) {
    Tag bo = get(dto.id);
    if (bo.getDefaultLanguage().equals(lang)) {
      bo.setLabel(dto.label);
      bo.setRecommended(dto.recommended);
      bo.setRestricted(dto.restricted);
      bo.setIconUrl(dto.iconUrl);
      em.flush();
    }
    return tService.updateEntityLanguageVersion(bo, dto, lang);
  }

  public Tag changeDefaultLanguage(Long id, LanguageVersion language) {
    Tag bo = get(id);
    Tag translation = tService.translateEntity(bo, language);
    bo.setDefaultLanguage(language);
    bo = BeanUtils.copyNotNullProperties(translation, bo);
    return em.merge(bo);
  }

  public void delete(Long id) {
    Tag tag = get(id);
    em.createQuery("delete from SightEventTag where tag=:tag")
        .setParameter("tag", tag).executeUpdate();
    em.remove(tag);
  }

  public void deleteLanguageVersion(Long id, LanguageVersion lang) {
    tService.deleteEntityTranslations(get(id), lang);
  }

  public List<SightEventTag> getFor(List<SightEvent> sightEvents) {
    if (sightEvents.isEmpty()) {
      return Collections.emptyList();
    }
    return em
        .createQuery("from SightEventTag where sightEvent in (:sightEvents)",
            SightEventTag.class)
        .setParameter("sightEvents", sightEvents).getResultList();
  }
}
