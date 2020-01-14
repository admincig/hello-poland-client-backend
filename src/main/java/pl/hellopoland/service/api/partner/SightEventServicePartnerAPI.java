package pl.hellopoland.service.api.partner;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
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
public class SightEventServicePartnerAPI {

  @Inject
  SightEventService service;
  @Inject
  CategoryService catService;
  @Inject
  TagService tagService;
  @Inject
  SightEventCategoryService secService;
  @Inject
  SightEventTagService setService;
  @Inject
  TranslationService tService;

  @RolesAllowed("partner")
  public SightEventDTO create(SightEventDTO dto) {
    SightEvent bo = service.create(dto, null);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightEventDTO createLanguageVesrion(SightEventDTO dto, LanguageVersion language) {
    return DtoMapper.getFullDTO(service.createLanguageVersionForLoggedUser(dto, language));
  }

  @RolesAllowed("partner")
  public SightEventDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion lang = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    SightEvent bo = service.getForLoggedUser(id);
    bo = tService.translateEntity(bo, lang);
    Set<Category> categories = bo.getCategories().stream().map(SightEventCategory::getCategory)
        .collect(Collectors.toSet());
    tService.translateEntities(categories, lang);
    var dto = DtoMapper.getFullDTO(bo);
    return dto;
  }

  @RolesAllowed("partner")
  public PagedCollection getList(SightEventPagedCollectionConfig config,
      String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    config.onlyCurrentPartner(true);
    config.onlyActive();
    PagedEntityCollection<SightEvent> bos = service.getList(config, language);
    var dtos = bos.items.stream().map(bo -> {
      var dto = DtoMapper.getDTO(bo);
      dto.language = bo.getDefaultLanguage().getLanuage();
      return dto;
    }).collect(Collectors.toList());
    if (language != null) {
      dtos.forEach(dto -> dto.language = language.getLanuage());
    }
    service.fetchTicketPoolDefinitions(bos.items, dtos, true, false);
    return new PagedCollection(dtos, bos.config);
  }

  @RolesAllowed("partner")
  public SightEventDTO update(SightEventDTO dto, LanguageVersion language) {
    SightEvent bo = service.updateForLoggedUser(dto, language);
    return DtoMapper.getFullDTO(bo);
  }


  @RolesAllowed("partner")
  public SightEventDTO uploadMainImage(Long id, byte[] icon) {
    SightEvent bo = service.uploadMainImageForLoggedUser(id, icon);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true, false);
    return dto;
  }

  @RolesAllowed("partner")
  public SightEventDTO uploadImage(Long id, byte[] img) {
    SightEvent bo = service.addImageToSightEventGalleryForLoggedUser(id, img);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true, false);
    return dto;
  }

  @RolesAllowed("partner")
  public SightEventDTO removeImageFromGallery(Long id, Long imgId) {
    SightEvent bo = service.removeImageFromGalleryForLoggedUser(id, imgId);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public SightEventDTO uploadPdf(Long id, byte[] pdf) {
    SightEvent bo = service.uploadPdfForLoggedUser(id, pdf);
    var dto = DtoMapper.getFullDTO(bo);
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), true, false);
    return dto;
  }

  @RolesAllowed("partner")
  public void deletePdf(Long id) {
    service.deletePdfForLoggedUser(id);
  }

  @RolesAllowed("partner")
  public void stopSale(Long sightId, Long ticketPoolDefId, Date date) {
    service.stopSale(sightId, ticketPoolDefId, date);
  }

  @RolesAllowed("partner")
  public SightEventDTO changeDefaultLanguage(Long id, LanguageVersion language) {
    SightEvent bo = service.changeDefaultLanguageForLoggedUser(id, language);
    return DtoMapper.getFullDTO(bo);
  }

  @RolesAllowed("partner")
  public void delete(Long id) {
    service.deleteForLoggedUser(id);
  }

  @RolesAllowed("partner")
  public void delete(Long id, LanguageVersion language) {
    service.deleteForLoggedUser(id, language);
  }

  @RolesAllowed("partner")
  public SightEventDTO addCategory(Long id, Long categoryId) {
    categoryRestrictionCheck(categoryId);
    SightEvent se = service.get(id);
    Category cat = catService.get(categoryId);
    se = secService.addCategory(se, cat);
    return DtoMapper.getFullDTO(se);
  }

  @RolesAllowed("partner")
  public SightEventDTO removeCategory(Long id, Long categoryId) {
    categoryRestrictionCheck(categoryId);
    SightEvent se = service.get(id);
    Category cat = catService.get(categoryId);
    se = secService.removeCategory(se, cat);
    return DtoMapper.getFullDTO(se);
  }

  private void categoryRestrictionCheck(Long id) {
    if (catService.get(id).isRestricted()) {
      throw new EJBAccessException();
    }
  }

  @RolesAllowed("partner")
  public SightEventDTO addTag(Long id, Long tagId) {
    tagRestrictionCheck(tagId);
    SightEvent se = service.get(id);
    Tag tag = tagService.get(tagId);
    se = setService.addTag(se, tag);
    return DtoMapper.getFullDTO(se);
  }

  @RolesAllowed("partner")
  public SightEventDTO removeTag(Long id, Long tagId) {
    tagRestrictionCheck(tagId);
    SightEvent se = service.get(id);
    Tag tag = tagService.get(tagId);
    se = setService.removeTag(se, tag);
    return DtoMapper.getFullDTO(se);
  }

  private void tagRestrictionCheck(Long id) {
    if (tagService.get(id).isRestricted()) {
      throw new EJBAccessException();
    }
  }

}
