package pl.hellopoland.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.io.ByteArrayInputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Paths;
import java.text.Collator;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.OpeningHours;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Translation;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.AvailableTicketNumberAssociationDTO;
import pl.hellopoland.dto.PushDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketPoolDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.AccessDeniedException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.util.BeanUtils;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;
import pl.hellopoland.util.Triplet;

@LocalBean
@Stateless
public class SightEventService extends ServiceSuperclass {
  @Inject
  UserService userService;

  @Inject
  private ImageService iService;

  @Inject
  private SightService sightService;

  @Inject
  private PartnerService partnerService;

  @Inject
  private TicketDefinitionService ticketService;

  @Inject
  private OpeningHoursService oHoursService;

  @Inject
  private FileDescriptorService fdService;

  @Inject
  private TranslationService translationService;

  @Inject
  private CategoryService catService;

  @Inject
  private TagService tagService;

  public List<SightEvent> getAllActiveAndPublishedAndNotBlocked() {
    return em.createQuery(
        "from SightEvent where active is true and published is true and blocked is false",
        SightEvent.class).getResultList();
  }

  public PagedEntityCollection<SightEvent> getList(SightEventPagedCollectionConfig config,
      LanguageVersion language) {
    if (config.isCurrentPartner()) {
      config.setPartner(partnerService.findByUserEmail(ctx.getCallerPrincipal().getName()).getId());
    }
    List<SightEvent> sightEvents = getQuery(config).getResultList();
    sightEvents.stream().forEach(s -> {
      s.setFavourite(s.getUsers().contains(userService.getLoggedUser()));
    });
    if (!sightEvents.isEmpty() && config.isFetchCategories()) {
      List<SightEventCategory> categories = catService.getFor(sightEvents);
      Map<SightEvent, Set<SightEventCategory>> grouped = categories.stream()
          .collect(groupingBy(SightEventCategory::getSightEvent, toSet()));
      sightEvents.forEach(se -> se.setCategories(grouped.get(se)));
    }
    if (!sightEvents.isEmpty() && config.isFetchTags()) {
      List<SightEventTag> tags = tagService.getFor(sightEvents);
      Map<SightEvent, Set<SightEventTag>> grouped = tags.stream()
          .collect(groupingBy(SightEventTag::getSightEvent, toSet()));
      sightEvents.forEach(se -> se.setTags(grouped.get(se)));
    }
    if (language != null) {
      sightEvents = translationService.translateEntities(sightEvents, language);
    }
    // List<SightEvent> sightEvents = getQuery(config).getResultList().stream()
    // .sorted(sightEventDatesComparator()).collect(toList());
    Collections.sort(sightEvents, sightEventPromotionComparator()
        .thenComparing(sightEventNamesComparator()));
    return new PagedEntityCollection<>(sightEvents, config);
  }

  private Comparator<SightEvent> sightEventPromotionComparator() {
    return Comparator.nullsLast(Comparator.comparing(SightEvent::getPromotion,
        Comparator.nullsLast(Comparator.naturalOrder())));
  }

  private Comparator<SightEvent> sightEventNamesComparator() {
    return Comparator.comparing(SightEvent::getName, polishComparator());
  }

  public SightEvent get(Long id) {
    SightEvent se = em.find(SightEvent.class, id);
    if (se == null) {
      throw new ResourceNotFoundException();
    }
    se.fetchCollections();
    se.setFavourite(se.getUsers().contains(userService.getLoggedUser()));
    return se;
  }

  public void savePush(PushDTO push) {
    Partner partner = partnerService.findByToken(push.secret);
    push.sightEvents.forEach(sdto -> {
      create(sdto, partner);
    });
  }


  public void delete(Long id) {
    SightEvent bo = get(id);

    Partner partner = bo.getPartner();
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    helloTicket.deleteSightEvent(bo, partner.getHptToken());

    bo.setActive(false);
  }

  public SightEvent create(SightEventDTO dto, Partner partner) {
    if (dto.sightId == null) {
      throw new ConflictingException("sightId can't be null.");
    }
    if (partner == null) {
      partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    }
    Sight sight = sightService.get(dto.sightId);
    if (!sight.getPartner().getId().equals(partner.getId())) {
      throw new AccessDeniedException();
    }
    var defLang = dto.defaultLanguage;
    var availableLanguageVersions = dto.availableLanguageVersions;
    dto.generalAdmission = Boolean.TRUE.equals(dto.generalAdmission);
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    dto = helloTicket.addSightEvent(dto, partner.getHptToken());
    dto.defaultLanguage = defLang;
    dto.availableLanguageVersions = availableLanguageVersions;
    SightEvent bo = new SightEvent();
    DtoMapper.copy(dto, bo);
    iService.update(bo, dto.mainImage == null ? null : dto.mainImage.original);
    bo.setPortal(hpt);

    if (sight != null) {
      bo.setSight(sight);
      if (sight.getAgreements() != null && !sight.getAgreements().isEmpty()) {
        bo.setAgreements(Set.copyOf(sight.getAgreements()));
      }
    }

    bo.setPartner(partner);
    em.persist(bo);

    ArrayList<OpeningHours> oHoursList = getOpeningHoursCollectionFromDTO(dto);
    if (oHoursList != null && !oHoursList.isEmpty()) {
      oHoursList.stream().forEach(oh -> {
        oh.setSightEvent(bo);
        oHoursService.persist(oh);
      });
      bo.setOpeningHours(oHoursList);
    }
    em.refresh(sight);
    sightService.recreateSearchIndex(sight);
    recreateSearchIndex(bo);
    logger.log(Logger.Level.INFO, "Saved new sight event: " + bo.getName());
    return createLanguageVersion(DtoMapper.getDTO(bo), partner, bo.getDefaultLanguage());
  }

  private ArrayList<OpeningHours> getOpeningHoursCollectionFromDTO(SightEventDTO dto) {
    return Optional.ofNullable(dto.openingHours)
        .map(l -> l.stream().map(oh -> DtoMapper.copy(oh, new OpeningHours()))
            .collect(toCollection(ArrayList::new)))
        .orElse(null);
  }

  private SightEvent createLanguageVersion(SightEventDTO dto, Partner partner,
      LanguageVersion language) {
    return translationService.createEntityLanguageVersion(getForPartner(dto.id, partner), dto,
        language);
  }

  public SightEvent createLanguageVersion(SightEventDTO dto, LanguageVersion language) {
    SightEvent bo = get(dto.id);
    return translationService.createEntityLanguageVersion(bo, dto, language);
  }

  public SightEvent createLanguageVersionForLoggedUser(SightEventDTO dto,
      LanguageVersion language) {
    return translationService.createEntityLanguageVersion(getForLoggedUser(dto.id), dto, language);
  }

  public SightEvent updateForLoggedUser(SightEventDTO dto, LanguageVersion language) {
    SightEvent bo = getForLoggedUser(dto.id);
    return update(bo, dto, language);
  }

  public SightEvent update(SightEvent bo, SightEventDTO dto, LanguageVersion language) {
    if (!translationService.isTranslated(bo, language)) {
      // throw new ConflictingException(
      // "Translation for language " + language.getLanuage() + " doesn't exists");
      createLanguageVersion(dto, language);
    }
    if (bo.getDefaultLanguage().equals(language)) {
      if (bo.getPortal().getType() == Portal.Type.HELLOTICKET_CLOUD_1) {
        Partner partner = bo.getPartner();
        Portal hpt = getPortal("Hello Ticket Cloud");
        HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
        dto.id = bo.getHptId();
        dto = helloTicket.updateSightEvent(dto, partner.getHptToken());
      }
      DtoMapper.copy(dto, bo);
      oHoursService.remove(bo.getOpeningHours());
      ArrayList<OpeningHours> oHoursList = getOpeningHoursCollectionFromDTO(dto);
      if (oHoursList != null && !oHoursList.isEmpty()) {
        oHoursList.stream().forEach(oh -> {
          oh.setSightEvent(bo);
          oHoursService.persist(oh);
        });
      }
      bo.setOpeningHours(null);
      bo.setOpeningHours(oHoursList);
      Sight sight = em.merge(bo.getSight());
      em.refresh(sight);
      sightService.recreateSearchIndex(sight);
      recreateSearchIndex(bo);
      em.flush();
    }
    return translationService.updateEntityLanguageVersion(bo, dto, language);
  }

  public List<SightEvent> getForPartner() {
    Partner partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());

    return em.createQuery(
        "from SightEvent event where event.sight.partner=:partner order by event.id desc",
        SightEvent.class).setParameter("partner", partner).getResultList();
  }

  public SightEvent uploadMainImageForLoggedUser(Long id, byte[] icon) {
    SightEvent bo = getForLoggedUser(id);
    return uploadMainImage(bo, icon);
  }

  public SightEvent uploadMainImage(SightEvent bo, byte[] icon) {
    bo.setMainImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(icon), "jpeg", null));
    return bo;
  }

  public SightEvent addImageToSightEventGallery(Long id, byte[] img) {
    SightEvent bo = get(id);
    bo.addImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(img), "jpeg", null));
    return bo;
  }

  public SightEvent addImageToSightEventGalleryForLoggedUser(Long id, byte[] img) {
    SightEvent bo = getForLoggedUser(id);
    bo.addImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(img), "jpeg", null));
    return bo;
  }

  public SightEvent removeImageFromGallery(Long id, Long imgId) {
    SightEvent bo = get(id);
    ImageCollector img = iService.get(imgId);
    if (!bo.getImages().contains(img)) {
      throw new ConflictingException(
          "Image [id:" + imgId + " is not in gallery of sightevent[id:" + id + "].");
    }
    bo.removeImage(img);
    return bo;
  }

  public SightEvent removeImageFromGalleryForLoggedUser(Long id, Long imgId) {
    SightEvent bo = getForLoggedUser(id);
    ImageCollector img = iService.get(imgId);
    if (!bo.getImages().contains(img)) {
      throw new ConflictingException(
          "Image [id:" + imgId + " is not in gallery of sightevent[id:" + id + "].");
    }
    bo.removeImage(img);
    return bo;
  }

  public SightEvent getForLoggedUser(Long id) {
    Partner partner = partnerService.getLoggedPartner();
    return getForPartner(id, partner);
  }

  public SightEvent getForPartner(Long sightEventId, Partner partner) {
    SightEvent sightEvent =
        em.createQuery("from SightEvent where id=:id and partner=:partner", SightEvent.class)
            .setParameter("id", sightEventId).setParameter("partner", partner).getSingleResult();
    sightEvent.fetchCollections();
    return sightEvent;
  }

  public void deleteForLoggedUser(Long id) {
    getForLoggedUser(id).setActive(false);
    delete(id);
  }

  public void deleteForLoggedUser(Long id, LanguageVersion language) {
    translationService.deleteEntityTranslations(getForLoggedUser(id), language);
  }

  public void fetchTicketPoolDefinitions(Collection<SightEvent> bos,
      List<SightEventDTO> dtos, boolean showDeletedTPD) {
    if (hasAnyHptCloudEvent(bos)) {

      var partnersToSightEventsWithHptId = groupDtosWithHptIdByPartner(bos, dtos);

      for (var partnerToSightEventsWithHptId : partnersToSightEventsWithHptId.entrySet()) {
        Partner partner = partnerToSightEventsWithHptId.getKey();
        List<TicketPoolDefinitionDTO> tpds = downloadHptTpds(partner, showDeletedTPD);
        var tpdsGroupedBySightEventId = tpds.stream()
            .collect(groupingBy(pool -> pool.sightEventId));

        for (var hptIdToSightEvent : partnerToSightEventsWithHptId.getValue()) {
          hptIdToSightEvent.getRight().ticketPoolDefinitions =
              tpdsGroupedBySightEventId.get(hptIdToSightEvent.getLeft());
        }
      }

      for (var sightEventDto : dtos) {
        sightEventDto.minPrice = findMinPrice(sightEventDto);
      }
    } else {
      // TODO other portals
    }
  }

  private Integer findMinPrice(SightEventDTO sightEventDto) {
    Integer minPrice = null;
    if (sightEventDto.ticketPoolDefinitions != null) {
      minPrice = Integer.MAX_VALUE;
      for (var poolDef : sightEventDto.ticketPoolDefinitions) {
        poolDef.sightEventId = sightEventDto.id;
        for (var t : poolDef.ticketDefinitions) {
          minPrice = Math.min(minPrice, t.price);
        }
      }
    }
    return minPrice;
  }

  private List<TicketPoolDefinitionDTO> downloadHptTpds(Partner partner,
      boolean showDeletedAndOverdued) {
    HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
    Stream<TicketPoolDefinitionDTO> poolDefinitions =
        hpt.getTicketPoolDefinitions(partner.getHptToken()).stream();
    if (!showDeletedAndOverdued) {
      poolDefinitions = poolDefinitions
          .filter(tpd -> !tpd.deleted)
          .filter(tpd -> {
            return !tpd.isCyclic
                || tpd.frequencyData.endDate == null
                || tpd.frequencyData.endDate.after(new Date());
          });
    }
    return poolDefinitions.collect(toList());
  }

  private Map<Partner, List<Pair<Long, SightEventDTO>>> groupDtosWithHptIdByPartner(
      Collection<SightEvent> bos, List<SightEventDTO> dtos) {
    var groupedById = pairBosWithDtosByHptId(bos, dtos);
    var groupedByPartner = new HashMap<Partner, List<Pair<Long, SightEventDTO>>>();
    groupedById.forEach(triplet -> {
      Partner partner = triplet.second.getPartner();
      if (!groupedByPartner.containsKey(partner)) {
        groupedByPartner.put(partner, new ArrayList<>());
      }
      groupedByPartner.get(partner).add(new ImmutablePair<>(triplet.first, triplet.third));
    });
    return groupedByPartner;
  }

  private boolean hasAnyHptCloudEvent(Collection<SightEvent> bos) {
    return bos.stream()
        .anyMatch(se -> se.getPortal().getType().equals(Portal.Type.HELLOTICKET_CLOUD_1));
  }

  private List<Triplet<Long, SightEvent, SightEventDTO>> pairBosWithDtosByHptId(
      Collection<SightEvent> bos,
      List<SightEventDTO> dtos) {
    var grouped = new ArrayList<Triplet<Long, SightEvent, SightEventDTO>>();
    bos.forEach(bo -> {
      for (var dto : dtos) {
        if (bo.getId().equals(dto.id)) {
          grouped.add(new Triplet<>(bo.getHptId(), bo, dto));
          break;
        }
      }
    });
    return grouped;
  }

  @SuppressWarnings("deprecation")
  public AvailableTicketNumberAssociationDTO checkAvailability(Long sightEventId, Date fromDate,
      Date toDate) {
    HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
    AvailableTicketNumberAssociationDTO associationDTO =
        hpt.checkAvailabilityOfTicketsForSightEvent(get(sightEventId), fromDate, toDate);
    associationDTO.ticketPoolDefinitions.forEach(tpd -> {
      tpd.startDate.setYear(fromDate.getYear());
      tpd.startDate.setMonth(fromDate.getMonth());
      tpd.startDate.setDate(fromDate.getDate());
    });
    var tdExternalIds = new ArrayList<Long>();

    var tpdDTOs = associationDTO.ticketPoolDefinitions;
    if (tpdDTOs != null && !tpdDTOs.isEmpty()) {
      tpdDTOs.forEach(tpd -> tpd.ticketDefinitions.forEach(td -> tdExternalIds.add(td.id)));
    }

    var tpDTOs = associationDTO.ticketPools;
    if (tpDTOs != null && !tpDTOs.isEmpty()) {
      tpDTOs.forEach(tp -> tp.ticketDefinitions.forEach(td -> tdExternalIds.add(td.id)));
    }

    var tdOBs = ticketService.getTicketsByExternalIds(tdExternalIds).stream().distinct()
        .collect(toList());

    if (tpdDTOs != null && !tpdDTOs.isEmpty()) {
      for (var tpdDto : tpdDTOs) {
        var tdDtos = tpdDto.ticketDefinitions;
        for (var tdDto : tdDtos) {
          for (var tdBo : tdOBs) {
            if (tdBo.getExternalId().equals(tdDto.id) && tdBo.getPoolId().equals(tpdDto.id)) {
              tdDto.id = tdBo.getId();
              break;
            }
          }
        }
      }
    }
    if (tpDTOs != null && !tpDTOs.isEmpty()) {
      for (var tpDto : tpDTOs) {
        var tdDtos = tpDto.ticketDefinitions;
        for (var tdDto : tdDtos) {
          for (var tdBo : tdOBs) {
            if (tdBo.getExternalId().equals(tdDto.id)
                && tdBo.getPoolId().equals(tpDto.ticketPoolDefinitionId)) {
              tdDto.id = tdBo.getId();
              break;
            }
          }
        }
      }
    }
    return associationDTO;
  }

  public SightEvent uploadPdfForLoggedUser(Long id, byte[] pdf) {
    SightEvent bo = getForLoggedUser(id);
    return uploadPdf(bo, pdf);
  }

  public SightEvent uploadPdf(SightEvent bo, byte[] pdf) {
    bo.setPdfAttachment(fdService.storeFileDescriptor(new ByteArrayInputStream(pdf), "pdf"));
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    helloTicket.addPdfToSightEvent(bo.getHptId(), DtoMapper.getFullDTO(bo.getPdfAttachment()),
        bo.getPartner().getHptToken());
    return bo;
  }

  public void deletePdfForLoggedUser(Long id) {
    SightEvent bo = getForLoggedUser(id);
    deletePdf(bo);
  }

  public void deletePdf(SightEvent bo) {
    var pdf = bo.getPdfAttachment();
    if (pdf != null) {
      fdService.deleteFile(Paths.get(pdf.getPath()));
      Portal hpt = getPortal("Hello Ticket Cloud");
      HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
      helloTicket.deletePdfFromSightEvent(bo, bo.getPartner().getHptToken());
      bo.setPdfAttachment(null);
      return;
    }
    logger.log(Level.INFO, "SightEvent [id=" + bo.getId() + "] doesn't have a pdf file ");
  }

  public boolean isAvailable(SightEventDTO dto, Date fromDate, Date toDate) {
    List<TicketPoolDefinitionDTO> tpds = dto.ticketPoolDefinitions;
    if (tpds != null && !tpds.isEmpty()) {
      return !tpds.stream()
          .filter(tpd -> !tpd.deleted
              && isInDateRange(tpd, fromDate, toDate)
              && ticketAreAvailable(dto, tpd, fromDate, toDate))
          .collect(toList()).isEmpty();
    }
    return false;
  }

  private boolean ticketAreAvailable(SightEventDTO dto, TicketPoolDefinitionDTO tpd, Date fromDate,
      Date toDate) {
    AvailableTicketNumberAssociationDTO availableTickets = null;
    if (toDate == null) {
      if (tpd.isCyclic) {
        // if (tpd.frequencyData.endDate == null) {
        return true;
        // }
        // availableTickets = checkAvailability(dto.id, tpd.startDate, tpd.frequencyData.endDate);
      } else {
        availableTickets = checkAvailability(dto.id, tpd.startDate, null);
      }
    } else {
      availableTickets = checkAvailability(dto.id, fromDate, toDate);
    }

    Stream<TicketPoolDefinitionDTO> s1 =
        availableTickets.ticketPoolDefinitions.stream().filter(tp -> tp.ticketDefinitions.stream()
            .filter(td -> !td.availableTicketsNumber.equals(Integer.valueOf(0))).count() != 0);
    Stream<TicketPoolDTO> s2 =
        availableTickets.ticketPools.stream().filter(tp -> tp.ticketDefinitions.stream()
            .filter(td -> !td.availableTicketsNumber.equals(Integer.valueOf(0))).count() != 0);

    return s1.count() != 0l || s2.count() != 0l;

    // return availableTickets.ticketPoolDefinitions.stream()
    // .filter(f -> f.availableTicketsNumber != 0).count() != 0l
    // || availableTickets.ticketPools.stream().filter(f -> f.availableTicketsNumber != 0)
    // .count() != 0l;
  }

  private boolean isInDateRange(TicketPoolDefinitionDTO tpd, Date fromDate, Date toDate) {
    if (fromDate == null) {
      fromDate = new Date();
    }
    var tpdStartDate = tpd.startDate;
    if (tpd.isCyclic) {
      return (fromDate.before(tpdStartDate)
          || ((tpd.frequencyData.endDate != null ? fromDate.before(tpd.frequencyData.endDate)
              : true)))
          && (toDate != null ? toDate.after(tpdStartDate) : true);
    }

    return ((tpd.wholeDay && fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        .isEqual(tpdStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
        || fromDate.before(tpdStartDate)) && (toDate != null ? toDate.after(tpdStartDate) : true);
  }

  public void stopSale(Long sightId, Long ticketPoolDefId, Date date) {
    var bo = getForLoggedUser(sightId);
    HelloTicket ht = new HelloTicket(bo.getPortal().getUrl());
    ht.stopSale(getLoggedPartner().getHptToken(), bo.getHptId(), ticketPoolDefId, date);
  }

  public SightEvent changeDefaultLanguageForLoggedUser(Long id, LanguageVersion language) {
    SightEvent bo = getForLoggedUser(id);
    return changeDefaultLanguage(bo, language);
  }

  public SightEvent changeDefaultLanguage(SightEvent bo, LanguageVersion language) {
    if (!translationService.isTranslated(bo, language)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + language.getLanuage()
              + "doesn't exists");
    }
    SightEvent translation = translationService.translateEntity(bo, language);
    bo.setDefaultLanguage(language);
    bo = BeanUtils.copyNotNullProperties(translation, bo);
    em.merge(bo);
    bo = get(bo.getId());
    if (bo.getPortal().getType() == Portal.Type.HELLOTICKET_CLOUD_1) {
      Partner partner = bo.getPartner();
      Portal hpt = getPortal("Hello Ticket Cloud");
      HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
      helloTicket.updateSightEvent(DtoMapper.getDTO(bo), partner.getHptToken());
    }
    sightService.recreateSearchIndex(bo.getSight());
    recreateSearchIndex(bo);
    return bo;
  }

  public void setSightEventPromotion(Long id, Integer promotion) {
    var bo = getOrThrow(id);
    em.createQuery("from SightEvent where promotion = :promotion", SightEvent.class)
        .setParameter("promotion", promotion).getResultList().forEach(se -> {
          se.setPromotion(null);
          em.flush();
        });
    bo.setPromotion(promotion);
  }

  public void removeSightEventPromotion(Long id) {
    var bo = getOrThrow(id);
    bo.setPromotion(null);
  }

  private SightEvent getOrThrow(Long id) throws ConflictingException {
    var bo = Optional.ofNullable(get(id))
        .orElseThrow(() -> new ConflictingException("Resource not found"));
    return bo;
  }

  public List<String> getCitiesForPublicEvents() {
    return em.createQuery(
        "select distinct location.city from SightEvent where active = true "
            + "and published = true and blocked = false and available = true and partner.blocked = false order by location.city asc",
        String.class).getResultStream()
        .map(String::strip)
        .filter(city -> !city.isBlank())
        .distinct()
        .sorted(Comparator.comparing(String::toLowerCase, polishComparator()))
        .collect(toList());
  }

  private Comparator<Object> polishComparator() {
    var collator = Collator.getInstance(new Locale("pl", "PL"));
    collator.setStrength(Collator.CANONICAL_DECOMPOSITION);
    return collator;
  }

  public void rebuildSearchIndices() {
    SightEventPagedCollectionConfig config = new SightEventPagedCollectionConfig();
    List<SightEvent> sightEvents = getQuery(config).getResultList();
    sightEvents.forEach(this::recreateSearchIndex);
  }

  private void recreateSearchIndex(SightEvent se) {
    {
      Set<String> words = se.getAvailableLanguageVersions().stream()
          .flatMap(
              lv -> translationService.getTranslations(se, lv).stream().map(Translation::getValue))
          .collect(toSet());
      se.recreateSearchIndex(words);
    }
  }

}
