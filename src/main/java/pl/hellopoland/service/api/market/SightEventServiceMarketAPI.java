package pl.hellopoland.service.api.market;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Category;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.bo.SightEventTag;
import pl.hellopoland.bo.Tag;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
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

@Stateless
public class SightEventServiceMarketAPI {

  @Inject
  SightEventService service;
  @Inject
  UserService userService;
  @Inject
  TicketPoolDefinitionService tpdService;

  @Inject
  private TranslationService translationService;

  @PermitAll
  public PagedCollection getList(SightEventPagedCollectionConfig config, Date fromDate, Date toDate,
      String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    if (fromDate != null && toDate != null && toDate.before(fromDate)) {
      throw new ConflictingException("toDate[" + toDate + "] is before fromDate[" + fromDate + "]");
    }
    config.onlyAvailable();
    config.onlyActive();
    config.onlyPublished();
    config.setOrderColumn("e.name");
    config.setOrderDirection("asc");
    PagedEntityCollection<SightEvent> bos = service.getList(config, language);
    bos.items = bos.items.stream().filter(se -> se.isAccessible()).collect(Collectors.toList());
    if (fromDate != null || toDate != null) {
      HelloTicket hptClient = new HelloTicket(service.getPortal("Hello Ticket Cloud").getUrl());
      bos.items = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(bos.items),
          fromDate, toDate);
    }
    List<SightEventDTO> dtos =
        bos.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public PagedCollection getPromoted(SightEventPagedCollectionConfig config,
      LanguageVersion language) {
    config.onlyAvailable();
    config.onlyActive();
    config.onlyPublished();
    config.setOrderColumn("e.name");
    config.setOrderDirection("asc");
    PagedEntityCollection<SightEvent> bos = service.getList(config, language);
    bos.items = bos.items.stream().filter(se -> se.isAccessible()).collect(Collectors.toList());
    HelloTicket hptClient = new HelloTicket(service.getPortal("Hello Ticket Cloud").getUrl());
    List<SightEvent> ses = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(bos.items),
        new Date(), null);
    List<SightEventDTO> dtos = ses.stream().map(DtoMapper::getDTO)
        .collect(Collectors.toList());
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public SightEventDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    SightEvent bo = service.get(id);
    SightEventDTO dto = null;
    if (bo.isAccessible()) {
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
      service.fetchTicketPoolDefinitions(List.of(bo), List.of(dto), false);
      if (service.isAvailable(dto, null, null)) {
        dto.similar = getSimilar(bo, language);
      }
    }
    // hiding
    dto.pdfAttachment = null;
    return dto;
  }

  @PermitAll
  public PagedCollection getRecommended(Integer count, LanguageVersion languageVersion) {
    SightEventPagedCollectionConfig config = prepareConfigForRandom(count);
    PagedEntityCollection<SightEvent> pc = service.getList(config, languageVersion);
    HelloTicket hptClient = new HelloTicket(service.getPortal("Hello Ticket Cloud").getUrl());
    List<SightEvent> ses = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(pc.items),
        new Date(), null);
    List<SightEventDTO> dtos = ses.stream().map(DtoMapper::getDTO)
        .collect(Collectors.toList());
    return new PagedCollection(dtos, pc.config);
  }

  private List<SightEventDTO> getSimilar(SightEvent bo, LanguageVersion language) {
    SightEventPagedCollectionConfig config = prepareConfigForRandom(6);
    config.setSight(bo.getSight());
    config.setExcludedIds(Set.of(bo.getId()));
    PagedEntityCollection<SightEvent> pc = service.getList(config, language);
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
    Date fromDateCurrentTime = fromDate != null
        ? Date.from(fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            .atTime(LocalTime.now()).atZone(ZoneId.systemDefault()).toInstant())
        : new Date();
    return fromDateCurrentTime;
  }

  private Date getToDateForEndDay(Date toDate) {
    Date toDateEndDay =
        toDate != null
            ? Date.from(toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant())
            : null;
    return toDateEndDay;
  }

  @PermitAll
  public PagedCollection getPersonalized(SightEventPagedCollectionConfig config, Integer count,
      LanguageVersion languageVersion) {
    if (config.getExcludedIds() == null || config.getExcludedIds().isEmpty()) {
      throw new ConflictingException("Cannot generate personalized Sight Events");
    }
    config.setPageSize(count);
    config.onlyActive();
    config.onlyPublished();
    config.onlyAvailable();
    PagedEntityCollection<SightEvent> pc = service.getList(config, languageVersion);

    HelloTicket hptClient = new HelloTicket(service.getPortal("Hello Ticket Cloud").getUrl());
    List<SightEvent> ses = hptClient.getSightEventsInDateRange(new ArrayList<SightEvent>(pc.items),
        new Date(), null);
    List<SightEventDTO> dtos = ses.stream().map(DtoMapper::getDTO)
        .collect(Collectors.toList());
    return new PagedCollection(dtos, pc.config);
  }

  @RolesAllowed("user")
  public SightEventDTO addFavourite(Long id) {
    SightEvent bo = service.get(id);
    bo.addUser(userService.getLoggedUser());
    return DtoMapper.getDTO(bo);
  }

  @RolesAllowed("user")
  public void removeFavourite(Long id) {
    SightEvent bo = service.get(id);
    if (!bo.getUsers().contains(userService.getLoggedUser())) {
      throw new ConflictingException("Sight event [id:" + id + "] not in favourites");
    }
    bo.removeUser(userService.getLoggedUser());
  }

  @RolesAllowed("user")
  public PagedCollection favourites(SightEventPagedCollectionConfig config, Date fromDate,
      Date toDate,
      String contentLanguageSymbol) {
    if (userService.getLoggedUser() != null) {
      config.onlyFavourite(userService.getLoggedUser().getId());
    }
    return getList(config, fromDate, toDate, contentLanguageSymbol);
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
      oro.availableDates.addAll(tpdService.getStartDates(tpd.id, date, halfYearFromNow));
    }
    asos.ticketPools.stream().forEach(tp -> {
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
