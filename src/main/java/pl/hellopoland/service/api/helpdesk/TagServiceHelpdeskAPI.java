package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.TagPagedCollectionConfig;
import pl.hellopoland.dto.TagDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.TagService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class TagServiceHelpdeskAPI {

  @Inject
  TagService service;
  @Inject
  TranslationService tService;

  @RolesAllowed("admin")
  public TagDTO create(TagDTO dto) {
    return DtoMapper.getFullDTO(service.create(dto));
  }

  @RolesAllowed("admin")
  public TagDTO createLanguageVesrion(TagDTO dto, LanguageVersion lang) {
    return DtoMapper.getFullDTO(service.createLanguageVesrion(dto, lang));
  }

  @RolesAllowed({"admin", "salesman"})
  public PagedCollection<TagDTO> pagedList(LanguageVersion language) {
    var config = new TagPagedCollectionConfig();
    var bos = service.pagedList(config);
    bos.items = tService.translateEntities(bos.items, language);
    List<TagDTO> dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection<>(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public TagDTO get(Long id, LanguageVersion language) {
    Tag cat = service.get(id);
    cat = tService.translateEntity(cat, language);
    return DtoMapper.getFullDTO(cat);
  }

  @RolesAllowed("admin")
  public TagDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    Tag bo = service.get(id);
    if (!tService.isTranslated(bo, language)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + language.getLanuage()
              + "doesn't exists");
    }
    bo = service.changeDefaultLanguage(id, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public TagDTO update(TagDTO dto, LanguageVersion lang) {
    Tag bo = service.update(dto, lang);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public void delete(long id) {
    service.delete(id);
  }

  @RolesAllowed("admin")
  public void deleteLanguageVersion(Long id, LanguageVersion lang) {
    service.deleteLanguageVersion(id, lang);
  }

  @RolesAllowed("admin")
  public TagDTO uploadIcon(Long id, byte[] bytes, String extension) {
    Tag tag = service.uploadIcon(id, bytes, extension);
    return DtoMapper.getFullDTO(tag);
  }
}
