package pl.hellopoland.service.api.market;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.SightEventCategory;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.enums.LanguageVersion;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.rest.dto.AvailableTicketNumberAssociationORO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.service.SightEventService;
import pl.hellopoland.service.TicketPoolDefinitionService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;

@Stateless
public class SightEventServiceMarketAPI {

  @Inject
  SightEventService service;

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
    List<SightEventDTO> dtos = bos.items.stream().map(bo -> {
      var dto = DtoMapper.getDTO(bo);
      dto.language = bo.getDefaultLanguage().getLanuage();
      return dto;
    }).collect(Collectors.toList());
    if (language != null) {
      dtos.forEach(dto -> dto.language = language.getLanuage());
    }
    return new PagedCollection(dtos, bos.config);
  }

  @PermitAll
  public SightEventDTO get(Long id, String contentLanguageSymbol) {
    LanguageVersion language = LanguageVersion.getForTranslationEntity(contentLanguageSymbol);
    SightEvent bo = service.get(id);
    SightEventDTO dto = null;
    if (bo.isAccessible()) {
      if (language != null) {
        bo = translationService.translateEntity(bo, language, true);
        translationService.translateEntities(bo.getCategories().stream()
            .map(SightEventCategory::getCategory).collect(Collectors.toSet()), language, false);
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
    return dto;
  }

  @PermitAll
  public PagedCollection getRecommended(Integer count, LanguageVersion languageVersion) {
    SightEventPagedCollectionConfig config = prepareConfigForRandom(6);
    PagedEntityCollection<SightEvent> pagedCollection = service.getList(config, languageVersion);
    return new PagedCollection(
        pagedCollection.items.stream().map(DtoMapper::getDTO).collect(Collectors.toList()),
        pagedCollection.config);
  }

  private List<SightEventDTO> getSimilar(SightEvent bo, LanguageVersion language) {
    SightEventPagedCollectionConfig config = prepareConfigForRandom(6);
    config.setSight(bo.getSight());
    return service.getList(config, language).items.stream().map(DtoMapper::getDTO)
        .collect(Collectors.toList());
  }

  private SightEventPagedCollectionConfig prepareConfigForRandom(Integer count) {
    SightEventPagedCollectionConfig config = new SightEventPagedCollectionConfig();
    config.setPageSize(count);
    config.setOrderColumn("random()");
    config.onlyActive();
    config.onlyPublished();
    return config;
  }

  @PermitAll
  public AvailableTicketNumberAssociationORO checkAvailability(Long sightEventId, Date fromDate,
      Date toDate) {
    return new AvailableTicketNumberAssociationORO(service.checkAvailability(sightEventId,
        getFromDateWithCurrentTime(fromDate), getToDateForEndDay(toDate)));
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

}
