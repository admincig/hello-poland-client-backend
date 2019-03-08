package pl.hellopoland.service;

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
import java.util.stream.Collectors;
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
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.AvailableTicketNumberAssociationDTO;
import pl.hellopoland.dto.PushDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.AccessDeniedException;
import pl.hellopoland.util.BeanUtils;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;
import pl.hellopoland.util.Triplet;

@LocalBean
@Stateless
public class SightEventService extends ServiceSuperclass {
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

  public PagedEntityCollection<SightEvent> getList(SightEventPagedCollectionConfig config) {
    if (config.isCurrentPartner()) {
      config.setPartner(partnerService.findByUserEmail(ctx.getCallerPrincipal().getName()).getId());
    }
    List<SightEvent> sightEvents = getQuery(config).getResultList();
    Collections.sort(sightEvents, sightEventNamesComparator(new Locale("pl_PL")));

    // List<SightEvent> sightEvents = getQuery(config).getResultList().stream()
    // .sorted(sightEventDatesComparator()).collect(toList());
    return new PagedEntityCollection<>(sightEvents, config);
  }

  private Comparator<SightEvent> sightEventNamesComparator(Locale locale) {
    var collator = Collator.getInstance(locale);
    collator.setStrength(Collator.CANONICAL_DECOMPOSITION);
    return Comparator.comparing(SightEvent::getName, collator);
  }

  public SightEvent get(Long id) {
    return em.find(SightEvent.class, id);
  }

  public void savePush(PushDTO push) {
    Partner partner = partnerService.findByToken(push.secret);
    push.sightEvents.forEach(sdto -> {
      create(sdto, partner);
    });
  }


  public void delete(Long id) {
    SightEvent bo = get(id);

    Partner partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
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
    if (!sight.getPartner().equals(partner)) {
      throw new AccessDeniedException();
    }
    var defLang = dto.defaultLanguage;
    dto.generalAdmission = Boolean.TRUE.equals(dto.generalAdmission);
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    dto = helloTicket.addSightEvent(dto, partner.getHptToken());
    dto.defaultLanguage = defLang;
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
    logger.log(Logger.Level.INFO, "Saved new sight event: " + bo.getName());
    return createLanguageVesrion(DtoMapper.getDTO(bo), partner, bo.getDefaultLanguage());
  }

  private ArrayList<OpeningHours> getOpeningHoursCollectionFromDTO(SightEventDTO dto) {
    return Optional.ofNullable(dto.openingHours)
        .map(l -> l.stream().map(oh -> DtoMapper.copy(oh, new OpeningHours()))
            .collect(Collectors.toCollection(ArrayList::new)))
        .orElse(null);
  }

  private SightEvent createLanguageVesrion(SightEventDTO dto, Partner partner,
      LanguageVersion language) {
    return translationService.createEntityLanguageVersion(getForPartner(dto.id, partner), dto,
        language);
  }

  public SightEvent createLanguageVesrion(SightEventDTO dto, LanguageVersion language) {
    return translationService.createEntityLanguageVersion(getForLoggedUser(dto.id), dto, language);
  }

  public SightEvent updateForLoggedUser(SightEventDTO dto) {
    SightEvent bo = getForLoggedUser(dto.id);
    if (bo.getPortal().getType() == Portal.Type.HELLOTICKET_CLOUD_1) {
      Partner partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
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
    return bo;
  }

  public SightEvent updateLanguageVersionForLoggedUser(SightEventDTO dto,
      LanguageVersion language) {
    return translationService.updateEntityLanguageVersion(getForLoggedUser(dto.id), dto, language);
  }

  public List<SightEvent> getForPartner() {
    Partner partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());

    return em.createQuery(
        "from SightEvent event where event.sight.partner=:partner order by event.id desc",
        SightEvent.class).setParameter("partner", partner).getResultList();
  }

  private Comparator<SightEvent> sightEventDatesComparator() {
    return new Comparator<>() {
      @Override
      public int compare(SightEvent sightEvent1, SightEvent sightEvent2) {
        Date current = new Date();

        if (sightEvent1.getDate() == null) {
          return -1;
        }
        if (sightEvent2.getDate() == null) {
          return 1;
        }
        if (areAllUpToDate(sightEvent1, sightEvent2, current)) {
          return sightEvent1.getDate().compareTo(sightEvent2.getDate());
        } else {
          return sightEvent1.getDate().compareTo(sightEvent2.getDate()) * -1;
        }
      }

      private boolean areAllUpToDate(SightEvent sightEvent1, SightEvent sightEvent2, Date current) {
        return sightEvent1.getDate().compareTo(current) > 0
            && sightEvent2.getDate().compareTo(current) > 0;
      }
    };
  }

  public SightEvent uploadMainImageForLoggedUser(Long id, byte[] icon) {
    SightEvent bo = getForLoggedUser(id);
    bo.setMainImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(icon), "jpeg", null));
    return bo;
  }

  public SightEvent addImageToSightEventGallery(Long id, byte[] img) {
    SightEvent bo = getForLoggedUser(id);
    bo.addImage(
        iService.validateAndStoreImageCollector(new ByteArrayInputStream(img), "jpeg", null));
    return bo;
  }

  public SightEvent removeImageFromGallery(Long id, Long imgId) {
    SightEvent bo = getForLoggedUser(id);
    ImageCollector img = iService.get(imgId);
    bo.removeImage(img);
    return bo;
  }

  public SightEvent getForLoggedUser(Long id) {
    Partner partner = partnerService.getLoggedPartner();
    return getForPartner(id, partner);
  }

  public SightEvent getForPartner(Long sightEventId, Partner partner) {
    return em.createQuery("from SightEvent where id=:id and partner=:partner", SightEvent.class)
        .setParameter("id", sightEventId).setParameter("partner", partner).getSingleResult();
  }

  public void deleteForLoggedUser(Long id) {
    getForLoggedUser(id).setActive(false);
    delete(id);
  }

  public void fetchTicketPoolDefinitions(Collection<SightEvent> bos,
      List<SightEventDTO> sightEventDtos, boolean showDeletedTPD) {

    if (hasAnyHptCloudEvent(bos)) {
      var pairedByIds = pairBosWithDtos(bos, sightEventDtos);
      var groupedByPartner = groupByPartner(pairedByIds);
      HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
      Map<Long, List<TicketDefinition>> externalIdToTicket = null;
      // Map<Long, TicketDefinition> externalIdToTicket = null;
      for (var entry : groupedByPartner.entrySet()) {
        Partner partner = entry.getKey();
        List<TicketPoolDefinitionDTO> poolDefinitions =
            hpt.getTicketPoolDefinitions(partner.getHptToken());
        if (!showDeletedTPD) {
          poolDefinitions =
              poolDefinitions.stream().filter(tpd -> !tpd.deleted).collect(Collectors.toList());
        }
        List<TicketDefinitionDTO> ticketDefinitions = new ArrayList<>();
        poolDefinitions.forEach(p -> ticketDefinitions.addAll(p.ticketDefinitions));
        List<TicketDefinition> ticketBos = ticketService.getTicketsByExternalIds(
            ticketDefinitions.stream().map(t -> t.id).collect(Collectors.toList()));
        if (externalIdToTicket == null) {
          externalIdToTicket =
              ticketBos.stream().collect(Collectors.groupingBy(TicketDefinition::getExternalId));
          // ticketBos.stream().collect(Collectors.toMap(TicketDefinition::getExternalId, t -> t));
        } else {
          externalIdToTicket.putAll(
              ticketBos.stream().collect(Collectors.groupingBy(TicketDefinition::getExternalId)));
          // .collect(Collectors.toMap(TicketDefinition::getExternalId, t -> t)));
        }
        var poolDefinitionsGroupedBySightEventId =
            poolDefinitions.stream().collect(Collectors.groupingBy(pool -> pool.sightEventId));
        List<Pair<Long, SightEventDTO>> sightEvents = entry.getValue();
        for (var pair : sightEvents) {
          pair.getRight().ticketPoolDefinitions =
              poolDefinitionsGroupedBySightEventId.get(pair.getLeft());
        }
      }
      for (var sightEventDto : sightEventDtos) {
        if (sightEventDto.ticketPoolDefinitions != null) {
          int minPrice = Integer.MAX_VALUE;
          for (var poolDefinitionDto : sightEventDto.ticketPoolDefinitions) {
            poolDefinitionDto.sightEventId = sightEventDto.id;
            for (var t : poolDefinitionDto.ticketDefinitions) {
              t.id = externalIdToTicket.get(t.id).stream()
                  .filter(tBo -> tBo.getPoolId().equals(poolDefinitionDto.id)).findFirst().get()
                  .getId();
              // t.id = externalIdToTicket.get(t.id).getId();
              minPrice = Math.min(minPrice, t.price);
            }
          }
          sightEventDto.minPrice = minPrice;
        }
      }
    } else {
      // TODO other portals
    }
  }

  private Map<Partner, List<Pair<Long, SightEventDTO>>> groupByPartner(
      List<Triplet<Long, SightEvent, SightEventDTO>> groupedById) {
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

  private List<Triplet<Long, SightEvent, SightEventDTO>> pairBosWithDtos(Collection<SightEvent> bos,
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

  public AvailableTicketNumberAssociationDTO checkAvailability(Long sightEventId, Date fromDate,
      Date toDate) {
    HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
    AvailableTicketNumberAssociationDTO associationDTO =
        hpt.checkAvailabilityOfTicketsForSightEvent(get(sightEventId), fromDate, toDate);

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
        .collect(Collectors.toList());

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

  public SightEvent uploadPdf(Long id, byte[] pdf) {
    SightEvent bo = getForLoggedUser(id);
    bo.setPdfAttachment(fdService.storeFileDescriptor(new ByteArrayInputStream(pdf), "pdf"));
    return bo;
  }

  public void deletePdf(Long id) {
    SightEvent bo = getForLoggedUser(id);
    var pdf = bo.getPdfAttachment();
    if (pdf != null) {
      fdService.deleteFile(Paths.get(pdf.getPath()));
      bo.setPdfAttachment(null);
      return;
    }
    logger.log(Level.INFO, "SightEvent [id=" + bo.getId() + "] doesn't have a pdf file ");
  }

  public boolean isAvailable(SightEventDTO dto, Date fromDate, Date toDate) {
    List<TicketPoolDefinitionDTO> tpds = dto.ticketPoolDefinitions;
    if (tpds != null && !tpds.isEmpty()) {
      return !tpds.stream()
          .filter(tpd -> !tpd.deleted && isInDateRange(tpd, fromDate, toDate)
              && ticketAreAvailable(dto, tpd, fromDate, toDate))
          .collect(Collectors.toList()).isEmpty();
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

  public SightEvent changeDefaultLanguage(Long id, LanguageVersion language) {
    SightEvent bo = getForLoggedUser(id);
    if (!translationService.isTranslated(bo, language)) {
      throw new ConflictingException(
          "Can not change the default language. Translation for language " + language.getLanuage()
              + "doesn't exists");
    }
    SightEvent translation = translationService.translateEntity(bo, language, true);
    bo.setDefaultLanguage(language);
    bo = BeanUtils.copyNotNullProperties(translation, bo);
    em.merge(bo);
    return bo;
  }

}
