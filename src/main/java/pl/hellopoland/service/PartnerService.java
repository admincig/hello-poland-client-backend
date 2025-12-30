package pl.hellopoland.service;

import pl.hellopoland.bo.*;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.dto.PartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.enums.BusinessType;
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
  @Inject
  PostalCodeDictionaryLookup pcd;


  private void enrichLocationDto(pl.hellopoland.dto.LocationDTO loc) {
       if (loc == null) return;

        // Fill only when missing, so UI can override if you want
      boolean missing =
                (loc.voivodeship == null || loc.voivodeship.isBlank()) ||
                        (loc.county == null || loc.county.isBlank()) ||
                        (loc.commune == null || loc.commune.isBlank());

        if (!missing) return;

        pcd.findByZipAndCity(loc.zipCode, loc.city).ifPresent(ad -> {
            if (loc.commune == null || loc.commune.isBlank()) loc.commune = ad.commune();
            if (loc.county == null || loc.county.isBlank()) loc.county = ad.county();
            if (loc.voivodeship == null || loc.voivodeship.isBlank()) loc.voivodeship = ad.voivodeship();
        });
    }


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
    partner.fetchRelations();
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

  public Partner uploadMainImage(Partner bo, byte[] icon, String extension) {
    bo.setMainImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(icon), extension));
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
        if (dto.location != null) {
            enrichLocationDto(dto.location);
        }
        if (bo.getAddress() == null) {
            bo.setAddress(new Address());
        }

        if (dto.location != null && !translationService.isTranslated(bo.getAddress(), lang)) {
            translationService.createEntityLanguageVersion(bo.getAddress(), dto.location, lang);
        }
        if (!translationService.isTranslated(bo, lang)) {
            bo = em.merge(bo);
            translationService.createEntityLanguageVersion(bo, dto, lang);
        }

        if (bo.getDefaultLanguage().equals(lang)) {
            DtoMapper.copy(dto, bo);
            if (dto.location != null) {
                bo.getAddress().setDirections(dto.location.directions);
            }
            em.flush();
        }

        if (dto.location != null) {
            translationService.updateEntityLanguageVersion(bo.getAddress(), dto.location, lang);
        }
        return translationService.updateEntityLanguageVersion(bo, dto, lang);
    }


  public Partner update(Partner bo, PartnerDTO dto, LanguageVersion lang) {
    enrichLocationDto(dto.location);
    enrichLocationDto(dto.correspondenceAddress);
    if (bo.getAddress() == null) {
          bo.setAddress(new Address());
    }
    if (dto.correspondenceAddress != null && bo.getCorrespondenceAddress() == null) {
          bo.setCorrespondenceAddress(new Address());
    }

    if (dto.location != null && !translationService.isTranslated(bo.getAddress(), lang)) {
      translationService.createEntityLanguageVersion(bo.getAddress(), dto.location, lang);
    }
    if (dto.correspondenceAddress != null
              && !translationService.isTranslated(bo.getCorrespondenceAddress(), lang)) {
          translationService.createEntityLanguageVersion(
                  bo.getCorrespondenceAddress(), dto.correspondenceAddress, lang);
    }


      if (!translationService.isTranslated(bo, lang)) {
      bo = em.merge(bo);
      translationService.createEntityLanguageVersion(bo, dto, lang);
    }

    if (dto.businessType != null) {
          bo.setBusinessType(BusinessType.getBusinessType(dto.businessType));
    }

      if (bo.getDefaultLanguage().equals(lang)) {
          DtoMapper.copy(dto, bo);
          bo.getAddress().setDirections(dto.location != null ? dto.location.directions : null);
          if (dto.correspondenceAddress != null) {
              bo.getCorrespondenceAddress().setDirections(dto.correspondenceAddress.directions);
          }
          em.flush();
      }

      if (dto.location != null) {
          translationService.updateEntityLanguageVersion(bo.getAddress(), dto.location, lang);
      }
      if (dto.correspondenceAddress != null) {
          translationService.updateEntityLanguageVersion(
                  bo.getCorrespondenceAddress(), dto.correspondenceAddress, lang);
      }
      return translationService.updateEntityLanguageVersion(bo, dto, lang);

  }

  public Partner createLanguageVersion(MarketPartnerDTO dto, LanguageVersion language) {
    enrichLocationDto(dto.location);

    Partner bo = get(dto.id);
    if (bo.getAddress() == null) {
      bo.setAddress(new Address());
    }
      if (dto.location != null) {
          translationService.createEntityLanguageVersion(bo.getAddress(), dto.location, language);
      }

    bo = em.merge(bo);
    return translationService.createEntityLanguageVersion(bo, dto, language);
  }

  public Partner createLanguageVersion(PartnerDTO dto, LanguageVersion language) {
    enrichLocationDto(dto.location);
    enrichLocationDto(dto.correspondenceAddress);
    Partner bo = get(dto.id);
    if (bo.getAddress() == null) {
      bo.setAddress(new Address());
    }
    translationService.createEntityLanguageVersion(bo.getAddress(), dto.location, language);
      if (dto.correspondenceAddress != null) {
          if (bo.getCorrespondenceAddress() == null) {
              bo.setCorrespondenceAddress(new Address());
          }
          translationService.createEntityLanguageVersion(
                  bo.getCorrespondenceAddress(), dto.correspondenceAddress, language);
      }
    bo = em.merge(bo);
    return translationService.createEntityLanguageVersion(bo, dto, language);
  }

  public Partner get(Long id) {
    Partner partner = em.find(Partner.class, id);
    partner.fetchRelations();
    return partner;
  }

  public void setBlocked(Partner bo, boolean blocked) {
    bo.setBlocked(blocked);
    em.flush();
  }

}
