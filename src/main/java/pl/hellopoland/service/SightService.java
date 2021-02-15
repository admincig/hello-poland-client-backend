package pl.hellopoland.service;

import pl.hellopoland.bo.*;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.ExceptionFactory;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.BeanUtils;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@LocalBean
@Stateless
public class SightService extends ServiceSuperclass {

  @Inject
  private ImageService imageService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private SightEventService sightEventService;

  @Inject
  private ExceptionFactory exceptionFactory;

  @Inject
  private OpeningHoursService oHoursService;

  @Inject
  private AgreementService agreementService;

  @Inject
  private TranslationService translationService;
  @Inject
  private UserService userService;

  public PagedEntityCollection<Sight> getList(SightPagedCollectionConfig config,
      LanguageVersion language) {
    if (config.isCurrentPartner()) {
      config.setPartner(partnerService.findByUserEmail(ctx.getCallerPrincipal().getName()).getId());
    }
    List<Sight> sights = getQuery(config).getResultList();
    sights.stream().forEach(s -> {
      s.setFavourite(s.getUsers().contains(userService.getLoggedUser()));
    });
    if (language != null) {
      sights = translationService.translateEntities(sights, language);
      if (config.isFetchSightEvents()) {
        sights.stream().forEach(s -> {
          s.setSightEvents(
              translationService.translateEntities(s.getSightEvents(), language));
        });
      }
    }
    Collections.sort(sights, getNamesComparator(Sight::getName, new Locale("pl_PL")));

    return new PagedEntityCollection<>(sights, config);
  }

  public Sight create(SightDTO dto, Partner partner) {
    Sight bo = new Sight();
    DtoMapper.copy(dto, bo);
    if (bo.isBlocked()) {
      bo.setPublished(false);
    }
    if (partner == null) {
      partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    }
    bo.setPartner(partner);
    em.persist(bo);

    imageService.handleImagesWhenCreating(bo, dto);

    ArrayList<OpeningHours> oHoursList = getOpeningHoursCollectionFromDTO(dto);
    if (oHoursList != null && !oHoursList.isEmpty()) {
      oHoursList.stream().forEach(oh -> {
        oh.setSight(bo);
        oHoursService.persist(oh);
      });
      bo.setOpeningHours(oHoursList);
    }
    if (Boolean.TRUE.equals(dto.generalAdmission)) {
      createGeneralAdmissionSightEvent(bo, partner);
    }
    var agreements = dto.agreements;
    if (agreements != null && !agreements.isEmpty()) {
      var agreementBos = Set.copyOf(agreementService.getForLoggedUser(
          agreements.stream().map(agrDto -> agrDto.id).collect(Collectors.toSet())));
      bo.setAgreements(agreementBos);
      var sightEventBos = bo.getSightEvents();
      if (sightEventBos != null && !sightEventBos.isEmpty()) {
        sightEventBos.forEach(se -> se.setAgreements(agreementBos));
      }
    }
    final var bo2 = createLanguageVersion(DtoMapper.getDTO(bo), bo.getDefaultLanguage());
    recreateSearchIndex(bo2);
    return bo2;
  }

  public Sight createLanguageVersion(SightDTO dto, LanguageVersion language) {
    return translationService.createEntityLanguageVersion(get(dto.id), dto,
        language);
  }

  public Sight get(Long id) {
    Sight bo = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", id).getSingleResult();
    bo.fetchCollections();
    bo.getSightEvents().forEach(SightEvent::fetchCollections);
    bo.setFavourite(bo.getUsers().contains(userService.getLoggedUser()));
    return bo;
  }

  public List<Sight> getActiveForPartner() {
    Partner partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    return em.createQuery(
        "from Sight sight where sight.active=true and sight.partner=:partner order by sight.id desc",
        Sight.class).setParameter("partner", partner).getResultList();
  }

  public List<Sight> getActiveForPartner(LanguageVersion language) {
    var bos = getActiveForPartner();
    if (language == null) {
      return bos;
    }
    return translationService.translateEntities(bos, language);
  }

  public Sight updateForLoggedUser(SightDTO dto, LanguageVersion language) {
    Sight bo = getActiveForLoggedPartner(dto.id);
    return update(bo, dto, language);
  }

  public Sight update(Sight bo, SightDTO dto, LanguageVersion language) {
    if (!translationService.isTranslated(bo, language)) {
      // throw new ConflictingException(
      // "Translation for language " + language.getLanuage() + " doesn't exists");
      createLanguageVersion(dto, language);
    }
    if (bo.getDefaultLanguage().equals(language)) {
      DtoMapper.copy(dto, bo);
      if (bo.isBlocked()) {
        bo.setPublished(false);
      }
      oHoursService.remove(bo.getOpeningHours());
      ArrayList<OpeningHours> oHoursList = getOpeningHoursCollectionFromDTO(dto);
      if (oHoursList != null && !oHoursList.isEmpty()) {
        oHoursList.stream().forEach(oh -> {
          oh.setSight(bo);
          oHoursService.persist(oh);
        });
      }
      bo.setOpeningHours(null);
      bo.setOpeningHours(oHoursList);
      var agreements = dto.agreements;
      if (agreements != null && !agreements.isEmpty()) {
        var agreementBos = Set.copyOf(agreementService.getForLoggedUser(
            agreements.stream().map(agrDto -> agrDto.id).collect(Collectors.toSet())));
        bo.setAgreements(agreementBos);
        var sightEventBos = bo.getSightEvents();
        if (sightEventBos != null && !sightEventBos.isEmpty()) {
          sightEventBos.forEach(se -> se.setAgreements(agreementBos));
        }
      }
    }
    final var bo2 = translationService.updateEntityLanguageVersion(get(dto.id), dto,
        language);
    recreateSearchIndex(em.merge(bo));
    return bo2;
  }

  private boolean hasActiveSightEvents(List<SightEvent> sightEvents) {
    for (SightEvent sightEvent : sightEvents) {
      if (sightEvent.isActive()) {
        return true;
      }
    }
    return false;
  }

  private void createGeneralAdmissionSightEvent(Sight sight, Partner partner) {
    SightEventDTO sed = DtoMapper.getGAEventDTO(sight);
    sightEventService.create(sed, partner);
  }

  public Sight getActiveForLoggedPartner(Long id) {
    Sight sight = em
        .createQuery("from Sight where id=:id and active=true and partner=:partner", Sight.class)
        .setParameter("id", id).setParameter("partner", getLoggedPartner()).getSingleResult();
    sight.fetchCollections();
    sight.getSightEvents().forEach(SightEvent::fetchCollections);

    return sight;
  }

  public Sight getActiveForLoggedUser(Long id, LanguageVersion language) {
    var bo = getActiveForLoggedPartner(id);
    if (language == null) {
      return bo;
    }
    return translationService.translateEntity(bo, language);
  }

  private ArrayList<OpeningHours> getOpeningHoursCollectionFromDTO(SightDTO dto) {
    return Optional.ofNullable(dto.openingHours)
        .map(l -> l.stream().map(oh -> DtoMapper.copy(oh, new OpeningHours()))
            .collect(Collectors.toCollection(ArrayList::new)))
        .orElse(null);
  }

  public void deleteForLoggedUser(Long id) {
    Sight bo = getActiveForLoggedPartner(id);
    delete(bo);
  }

  public void delete(Sight bo) {
    if (hasActiveSightEvents(bo.getSightEvents())) {
      throw exceptionFactory.sightHasAssignedSightEventsException();
    } else {
      bo.setActive(false);
    }
  }

  public void deleteForLoggedUser(Long id, LanguageVersion language) {
    translationService.deleteEntityTranslations(getActiveForLoggedPartner(id), language);
  }

  private Sight getForLoggedPartner(Long sightId) {
    var partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    Sight sight = em.createQuery("from Sight where partner = :partner and id = :id", Sight.class)
        .setParameter("partner", partner).setParameter("id", sightId).getResultStream().findFirst()
        .orElseThrow(ResourceNotFoundException::new);
    sight.fetchCollections();
    sight.getSightEvents().forEach(SightEvent::fetchCollections);
    return sight;
  }

  public Sight changeDefaultLanguageForLoggedUser(Long id, LanguageVersion language) {
    Sight bo = getForLoggedPartner(id);
    return changeDefaultLanguage(bo, language);
  }

  public Sight changeDefaultLanguage(Sight bo, LanguageVersion language) {
    if (!translationService.isTranslated(bo, language)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + language.getLanuage()
              + "doesn't exists");
    }
    Sight translation = translationService.translateEntity(bo, language);
    bo.setDefaultLanguage(language);
    bo = BeanUtils.copyNotNullProperties(translation, bo);
    em.merge(bo);
    bo = get(bo.getId());
    recreateSearchIndex(bo);
    return bo;
  }

  public void rebuildSearchIndices() {
    SightPagedCollectionConfig config = new SightPagedCollectionConfig();
    List<Sight> sightEvents = getQuery(config).getResultList();
    sightEvents.forEach(this::recreateSearchIndex);
  }

  public void recreateSearchIndex(Sight s) {
    Set<String> words = s.getAvailableLanguageVersions().stream()
        .flatMap(
            lv -> translationService.getTranslations(s, lv).stream().map(Translation::getValue))
        .collect(Collectors.toSet());
    s.recreateSearchIndex(words);
  }
}
