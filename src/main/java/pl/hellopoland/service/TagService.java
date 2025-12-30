package pl.hellopoland.service;

import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.TagPagedCollectionConfig;
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.BeanUtils;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.io.ByteArrayInputStream;
import pl.hellopoland.service.FileDescriptorService;
import java.util.*;

@Stateless
public class TagService extends ServiceSuperclass {

  @Inject
  private TranslationService tService;
  @Inject
  private ImageService iService;

  @Inject
  FileDescriptorService fileDescriptorService;

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
    Tag tag = em.find(Tag.class, id);
    return Optional.ofNullable(tag).map(t -> {
      t.getAvailableLanguageVersions().size();
      return t;
    }).orElseThrow(() -> new ResourceNotFoundException());
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

    public Tag uploadIcon(Long id, byte[] bytes, String extension) {
        Tag tag = get(id);

        // SVG: zapisujemy "as-is" i ustawiamy iconUrl na bezpośredni plik
        if ("svg".equalsIgnoreCase(extension)) {
            String hash = java.util.UUID.randomUUID().toString().replace('-', 'x');
            String basePath = properties.getProperty("dms.root.path")
                    + java.io.File.separator + "icons"
                    + java.io.File.separator + hash.substring(0, 1)
                    + java.io.File.separator + hash.substring(1, 2)
                    + java.io.File.separator;

            java.io.File file =
                    fileDescriptorService.createEmptyFileOnDisc(basePath + hash + ".svg");
            try {
                java.nio.file.Files.write(file.toPath(), bytes);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Icon SVG NOT stored: " + e.getMessage(), e);
            }

            tag.setIconUrl(System.getProperty("base.url") + "/images/" + hash + ".svg");

            em.flush();
            return tag;
        }

        // PNG/JPG: zostaje jak było
        ImageCollector ic = iService.storeImageCollector(new java.io.ByteArrayInputStream(bytes), extension);
        tag.setIconUrl(ic.getQvga().getDownloadUrl());
        em.flush();
        return tag;
    }

}
