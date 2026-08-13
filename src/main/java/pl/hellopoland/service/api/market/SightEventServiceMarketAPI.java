package pl.hellopoland.service.api.market;

import pl.hellopoland.bo.*;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;
import pl.hellopoland.rest.dto.AvailableDatesORO;
import pl.hellopoland.rest.dto.AvailableTicketNumberAssociationORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TicketPoolDefinitionService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.service.UserService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class SightEventServiceMarketAPI {

  @Inject
  SightEventService service;
  @Inject
  UserService userService;
  @Inject
  TicketPoolDefinitionService tpdService;
  @Inject
  SightEventServiceMarketAPI seMarketApiService;

  @Inject
  private TranslationService translationService;

  @PermitAll
  public PagedCollection<SightEventDTO> getList(SightEventPagedCollectionConfig config) {
    config.onlyAvailable();
    config.onlyActive();
    config.onlyPublished();
    PagedEntityCollection<SightEvent> bos = service.getList(config);
    bos.items = bos.items.stream()
        .filter(se -> se.isAccessible())
        .collect(Collectors.toList());
    List<SightEventDTO> dtos = bos.items.stream()
        .map(DtoMapper::getDTO)
        .collect(Collectors.toList());
    service.fetchTicketPoolDefinitions(bos.items, dtos, false, true);
    dtos = dtos.stream()
        .filter(dto -> service.isAvailable(dto, null, null))
        .collect(Collectors.toList());

    return new PagedCollection<>(dtos, bos.config);
  }

  @PermitAll
  public SightEventDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    SightEvent bo = service.get(id);
    SightEventDTO dto = null;
    if (!bo.isAccessible()) {
      throw new ResourceNotFoundException();
    }
    if (language != null) {
      bo = translationService.translateEntity(bo, language);
      Set<Category> categories = bo.getCategories().stream()
          .map(SightEventCategory::getCategory).collect(Collectors.toSet());
      translationService.translateEntities(categories, language);
      Set<Tag> tags = bo.getTags().stream()
          .map(SightEventTag::getTag).collect(Collectors.toSet());
      translationService.translateEntities(tags, language);
    } else {
      language = bo.getDefaultLanguage();
    }
    dto = DtoMapper.getFullDTO(bo);
    dto.partnerAffiliateCode = null;
    dto.language = language.getLanuage();
    service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), false, true);
    if (service.isAvailable(dto, null, null)) {
      dto.similar = getSimilar(bo, language);
    }
    // hiding
    dto.pdfAttachment = null;
    return dto;
  }

  private List<SightEventDTO> getSimilar(SightEvent bo, LanguageVersion language) {
    SightEventPagedCollectionConfig config = prepareConfigForRandom(6);
    config.setSight(bo.getSight());
    config.setExcludedIds(Set.of(bo.getId()));
    config.setLanguage(language);
    PagedEntityCollection<SightEvent> pc = service.getList(config);
    HelloTicket hptClient = new HelloTicket(service.getPortal("Hello Ticket Cloud").getUrl());
    List<SightEvent> ses = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(pc.items),
        new Date(), null);
    return ses.stream().map(DtoMapper::getDTO)
        .collect(Collectors.toList());
  }

  private SightEventPagedCollectionConfig prepareConfigForRandom(Integer count) {
    SightEventPagedCollectionConfig config = new SightEventPagedCollectionConfig();
    config.setPageSize(count);
    config.setOrderColumn("random()");
    config.onlyActive();
    config.onlyPublished();
    config.onlyAvailable();
    config.setDateFrom(new Date());
    return config;
  }

  @PermitAll
  public AvailableTicketNumberAssociationORO checkAvailability(Long sightEventId, Date fromDate,
      Date toDate) {
    fromDate = getFromDateWithCurrentTime(fromDate);
    toDate = getToDateForEndDay(toDate);
    var asos = service.checkAvailability(sightEventId, fromDate, toDate);
    return new AvailableTicketNumberAssociationORO(asos);
  }

  private Date getFromDateWithCurrentTime(Date fromDate) {
    return fromDate != null
        ? Date.from(fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            .atTime(LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant())
        : new Date();
  }

  private Date getToDateForEndDay(Date toDate) {
    return toDate != null
        ? Date.from(toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            .atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant())
        : null;
  }

  @PermitAll
  public PagedCollection<SightEventDTO> getPersonalized(SightEventPagedCollectionConfig config) {
    if (config.getExcludedIds() == null || config.getExcludedIds().isEmpty()) {
      throw new ConflictingException(
          "Cannot generate personalized Sight Events. Fill 'sameCategorySightEventIds' array in config.");
    }
    PagedCollection<SightEventDTO> pc = getList(config);
    int missingAmount = config.getPageSize() - pc.items.size();
    if (missingAmount > 0) {
      config.setPageSize(missingAmount);
      pc.items.addAll(fetchRandomOfNumberAndWithout(config, pc.items));
    }
    return pc;
  }

  public Collection<SightEventDTO> fetchRandomOfNumberAndWithout(
      SightEventPagedCollectionConfig config,
      Collection<SightEventDTO> excluded) {
    SightEventPagedCollectionConfig missingSightsConfig =
        prepareConfigForRandom(config.getPageSize());
    // excluding all ids
    Set<Long> alreadyExcluded = config.getExcludedIds();
    Set<Long> excludedIds = excluded.stream()
        .map(dto -> dto.id)
        .collect(Collectors.toSet());
    excludedIds.addAll(alreadyExcluded);
    missingSightsConfig.setExcludedIds(excludedIds);
    missingSightsConfig.setLanguage(config.getLanguage());
    PagedCollection<SightEventDTO> missingPc = getList(missingSightsConfig);
    return missingPc.items;
  }

  @RolesAllowed("user")
  public void addFavourite(Long id) {
    SightEvent bo = service.get(id);
    bo.addUser(userService.getLoggedUser());
  }

  @RolesAllowed("user")
  public void removeFavourite(Long id) {
    SightEvent bo = service.get(id);
    if (!bo.getUsers().contains(userService.getLoggedUser())) {
      throw new ConflictingException("Sight event [id:" + id + "] not in favourites");
    }
    bo.removeUser(userService.getLoggedUser());
  }

  @PermitAll
  public AvailableDatesORO checkAvailableDates(Long id, LocalDate date) {
    if (date == null) {
      date = LocalDate.now();
    }
    LocalDate halfYearFromNow = LocalDate.now().plusMonths(6);

    var asos = service.checkAvailability(id,
        Date.from(date.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()),
        Date.from(halfYearFromNow.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()));
    AvailableDatesORO oro = new AvailableDatesORO();
    for (var tpd : asos.ticketPoolDefinitions) {
      boolean noMoreTickets = tpd.availableTicketsNumber.equals(0)
          || tpd.ticketDefinitions.stream()
              .allMatch(ticket -> ticket.availableTicketsNumber.equals(0));
      if (service.isVisibleOnPortal(tpd) && !noMoreTickets) {
        oro.availableDates.addAll(tpdService.getStartDates(tpd.id, date, halfYearFromNow));
      }
    }
    asos.ticketPools.stream().filter(service::isVisibleOnPortal).forEach(tp -> {
      LocalDate ld = tp.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      boolean noMoreTickets = tp.availableTicketsNumber.equals(0)
          || tp.ticketDefinitions.stream().allMatch(td -> td.availableTicketsNumber.equals(0));
      if (noMoreTickets) {
        oro.availableDates.remove(ld);
      } else {
        oro.availableDates.add(ld);
      }
    });
    return oro;
  }

}
