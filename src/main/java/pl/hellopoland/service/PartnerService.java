package pl.hellopoland.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.dto.MarketPartnerDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.util.BeanUtils;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.Located;
import pl.hellopoland.util.PagedEntityCollection;

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

  public Partner findByToken(String token) {
    return em.createQuery("select partner from Partner partner where partner.hptToken=:token",
        Partner.class).setParameter("token", token).getSingleResult();
  }

  public List<Partner> getAll() {
    return em.createQuery("from Partner order by id asc", Partner.class).getResultList();
  }

  public PagedEntityCollection<Partner> getList(PartnerPagedCollectionConfig config) {
    List<Partner> list = getQuery(config).getResultList();
    return new PagedEntityCollection<>(list, config);
  }

  public Partner getPartnerWithCategoriesAndCities(Long id) {
    Partner partner = em.find(Partner.class, id);
    List<Category> categories =
        partner.getSight().stream().flatMap(sight -> sight.getSightEvents().stream())
            .flatMap(se -> se.getCategories().stream()).map(SightEventCategory::getCategory)
            .collect(Collectors.toList());
    partner.setCategories(categories);
    List<String> cities =
        Stream.<Located>concat(
            partner.getSight().stream(),
            partner.getSightEvents().stream())
            .map(se -> se.getLocation().getCity())
            .distinct()
            .collect(Collectors.toList());
    partner.setCities(cities);
    return partner;
  }

  public Partner uploadMainImage(Partner bo, byte[] icon) {
    bo.setMainImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(icon), "jpeg", null));
    return bo;
  }

  public Partner changeDefaultLanguage(Partner bo, LanguageVersion lang) {
    Partner translation = translationService.translateEntity(bo, lang, true);
    bo.setDefaultLanguage(lang);
    bo = BeanUtils.copyNotNullProperties(translation, bo);
    em.merge(bo);
    return bo;
  }

  public Partner update(Partner bo, MarketPartnerDTO dto, LanguageVersion lang) {
    if (!translationService.isTranslated(bo, lang)) {
      translationService.createEntityLanguageVersion(bo, dto, lang);
    }
    if (bo.getDefaultLanguage().equals(lang)) {
      DtoMapper.copy(dto, bo);
      em.flush();
    }
    return translationService.updateEntityLanguageVersion(bo, dto, lang);
  }

  public Partner createLanguageVersion(MarketPartnerDTO dto, LanguageVersion language) {
    Partner bo = em.find(Partner.class, dto.id);
    return translationService.createEntityLanguageVersion(bo, dto, language);
  }
}
