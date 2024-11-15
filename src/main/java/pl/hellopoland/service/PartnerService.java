package pl.hellopoland.service;

import pl.hellopoland.bo.*;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.BeanUtils;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.Located;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@LocalBean
@Stateless
public class PartnerService extends ServiceSuperclass {

  @Inject
  ImageService iService;
  @Inject
  TranslationService translationService;

  public Partner findByUserEmail(String email) {
    return em.createQuery(
        "select partner from User user join user.partner partner where lower(user.email) = :email",
        Partner.class).setParameter("email", email.toLowerCase()).getSingleResult();
  }

  public Map<Long, Partner> findByHptIds(List<Long> hptIds) {
    Map<Long, Partner> map = new HashMap<>();
    List<Partner> result =
        em.createQuery("from Partner where hptId in (:hptIds) order by id asc", Partner.class)
            .setParameter("hptIds", hptIds)
            .getResultList();
    result.forEach(res -> map.put(res.getHptId(), res));
    return map;
  }

  public List<Partner> getAll() {
    return em.createQuery("from Partner order by id asc", Partner.class).getResultList();
  }

  public PagedEntityCollection<Partner> getList(PartnerPagedCollectionConfig config) {
    List<Partner> list = getQuery(config).getResultList();
    return new PagedEntityCollection<>(list, config);
  }

  public Partner getPartnerWithCategoriesAndTagsAndCities(Long id) {
    Partner partner = em.find(Partner.class, id);
    partner.fetchCollections();
    List<Category> categories =
        partner.getSightEvents().stream()
            .flatMap(se -> se.getCategories().stream()).map(SightEventCategory::getCategory)
            .collect(Collectors.toList());
    partner.setCategories(categories);
    List<Tag> tags =
        partner.getSightEvents().stream()
            .flatMap(se -> se.getTags().stream()).map(SightEventTag::getTag)
            .collect(Collectors.toList());
    partner.setTags(tags);
    List<String> cities =
        Stream.<Located>concat(
            partner.getSight().stream(),
            partner.getSightEvents().stream())
            .map(se -> se.getLocation().getCity().strip())
            .distinct()
            .filter(city -> !city.isBlank())
            .collect(Collectors.toList());
    partner.setCities(cities);
    return partner;
  }

  public Partner uploadMainImage(Partner bo, byte[] icon) {
    bo.setMainImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(icon), "jpeg"));
    em.flush();
    return bo;
  }

  public Partner changeDefaultLanguage(Partner bo, LanguageVersion lang) {
    if (bo.getAddress() != null) {
      Address aTranslation = translationService.translateEntity(bo.getAddress(), lang);
      bo.getAddress().setDefaultLanguage(lang);
      bo.getAddress().setDirections(aTranslation.getDirections());
    }
    Partner translation = translationService.translateEntity(bo, lang);
    bo = BeanUtils.copyNotNullProperties(translation, bo);
    bo.setDefaultLanguage(lang);
    em.merge(bo);
    em.flush();
    return bo;
  }

  public Partner update(Partner bo, MarketPartnerDTO dto, LanguageVersion lang) {
    if (!translationService.isTranslated(bo.getAddress(), lang)) {
      translationService.createEntityLanguageVersion(bo.getAddress(), dto, lang);
    }
    if (!translationService.isTranslated(bo, lang)) {
      bo = em.merge(bo);
      translationService.createEntityLanguageVersion(bo, dto, lang);
    }
    if (bo.getDefaultLanguage().equals(lang)) {
      DtoMapper.copy(dto, bo);
      if (bo.getAddress() == null) {
        bo.setAddress(new Address());
      }
      bo.getAddress().setDirections(dto.location.directions);
      em.flush();
    }
    translationService.updateEntityLanguageVersion(bo.getAddress(), dto.location, lang);
    return translationService.updateEntityLanguageVersion(bo, dto, lang);
  }

  public Partner update(Partner bo, PartnerDTO dto, LanguageVersion lang) {
    if (!translationService.isTranslated(bo.getAddress(), lang)) {
      translationService.createEntityLanguageVersion(bo.getAddress(), dto.location, lang);
    }

    if (!translationService.isTranslated(bo, lang)) {
      bo = em.merge(bo);
      translationService.createEntityLanguageVersion(bo, dto, lang);
    }
    if (bo.getDefaultLanguage().equals(lang)) {
      DtoMapper.copy(dto, bo);
      bo.getAddress().setDirections(dto.location.directions);
      em.flush();
    }
    translationService.updateEntityLanguageVersion(bo.getAddress(), dto.location, lang);
    return translationService.updateEntityLanguageVersion(bo, dto, lang);
  }

  public Partner createLanguageVersion(MarketPartnerDTO dto, LanguageVersion language) {
    Partner bo = get(dto.id);
    if (bo.getAddress() == null) {
      bo.setAddress(new Address());
    }
    translationService.createEntityLanguageVersion(bo.getAddress(), dto.location, language);
    bo = em.merge(bo);
    return translationService.createEntityLanguageVersion(bo, dto, language);
  }

  public Partner createLanguageVersion(PartnerDTO dto, LanguageVersion language) {
    Partner bo = get(dto.id);
    if (bo.getAddress() == null) {
      bo.setAddress(new Address());
    }
    translationService.createEntityLanguageVersion(bo.getAddress(), dto.location, language);
    bo = em.merge(bo);
    return translationService.createEntityLanguageVersion(bo, dto, language);
  }

  public Partner get(Long id) {
    Partner partner = em.find(Partner.class, id);
    partner.fetchCollections();
    return partner;
  }

  public void setBlocked(Partner bo, boolean blocked) {
    bo.setBlocked(blocked);
    em.flush();
  }

}
