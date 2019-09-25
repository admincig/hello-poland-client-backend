package pl.hellopoland.service.api.helpdesk;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.CategoryService;
import pl.hellopoland.service.SightEventCategoryService;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.SightEventTagService;
import pl.hellopoland.service.TagService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightEventServiceHelpdeskAPI {

  @Inject
  private SightEventService service;
  @Inject
  private TranslationService tService;
  @Inject
  private SightEventCategoryService secService;
  @Inject
  private SightEventTagService setService;
  @Inject
  private CategoryService catService;
  @Inject
  private TagService tagService;



  @RolesAllowed("admin")
  public PagedCollection list(SightEventPagedCollectionConfig config,
      LanguageVersion language) {
    config.onlyActive();
    PagedEntityCollection<SightEvent> bos = service.getList(config, language);
    var dtos = bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("admin")
  public void setPromotion(Long id, Integer promotion) {
    service.setSightEventPromotion(id, promotion);
  }

  @RolesAllowed("admin")
  public void removePromotion(Long id) {
    service.removeSightEventPromotion(id);
  }

  @RolesAllowed("admin")
  public void delete(Long id) {
    service.delete(id);
  }

  @RolesAllowed("admin")
  public SightEventDTO update(SightEventDTO dto, LanguageVersion language) {
    SightEvent bo = service.get(dto.id);
    bo = service.update(bo, dto, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public SightEventDTO get(Long id, LanguageVersion language) {
    SightEvent bo = service.get(id);
    bo = tService.translateEntity(bo, language, true);
    tService.translateEntities(bo.getCategories().stream().map(SightEventCategory::getCategory)
        .collect(Collectors.toSet()), language, false);
    SightEventDTO dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), false);
    return dto;
  }

  @RolesAllowed("admin")
  public SightEventDTO createLanguageVesrion(SightEventDTO dto, LanguageVersion language) {
    SightEvent bo = service.createLanguageVersion(dto, language);
    return DtoMapper.getFullDTO(bo);
  }


  @RolesAllowed("admin")
  public SightEventDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    SightEvent bo = service.get(id);
    if (!tService.isTranslated(bo, language)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + language.getLanuage()
              + "doesn't exists");
    }
    bo = service.changeDefaultLanguage(bo, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("admin")
  public void deleteLanguageVersion(Long id, LanguageVersion language) {
    SightEvent bo = service.get(id);
    tService.deleteEntityTranslations(bo, language);
  }

  @RolesAllowed("admin")
  public SightEventDTO addCategory(Long id, Long categoryId) {
    SightEvent se = service.get(id);
    Category cat = catService.get(categoryId);
    se = secService.addCategory(se, cat);
    return DtoMapper.getFullDTO(se);
  }

  @RolesAllowed("admin")
  public SightEventDTO removeCategory(Long id, Long categoryId) {
    SightEvent se = service.get(id);
    Category cat = catService.get(categoryId);
    se = secService.removeCategory(se, cat);
    return DtoMapper.getFullDTO(se);
  }

  @RolesAllowed("admin")
  public SightEventDTO addTag(Long id, Long tagId) {
    SightEvent se = service.get(id);
    Tag tag = tagService.get(tagId);
    se = setService.addTag(se, tag);
    return DtoMapper.getFullDTO(se);
  }

  @RolesAllowed("admin")
  public SightEventDTO removeTag(Long id, Long tagId) {
    SightEvent se = service.get(id);
    Tag tag = tagService.get(tagId);
    se = setService.removeTag(se, tag);
    return DtoMapper.getFullDTO(se);
  }

  @RolesAllowed("admin")
  public SightEventDTO uploadPdf(Long id, byte[] pdf) {
    var bo = service.get(id);
    bo = service.uploadPdf(bo, pdf);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true);
    return dto;
  }

  @RolesAllowed("admin")
  public void deletePdf(Long id) {
    var bo = service.get(id);
    service.deletePdf(bo);
  }

  @RolesAllowed("admin")
  public SightEventDTO uploadMainImage(Long id, byte[] icon) {
    var bo = service.get(id);
    bo = service.uploadMainImage(bo, icon);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true);
    return dto;
  }

}
