package pl.hellopoland.service;

import static java.util.stream.Collectors.toList;
import java.io.ByteArrayInputStream;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.TicketDefinition;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.PushDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.dto.TicketDefinitionDTO;
import pl.hellopoland.dto.TicketPoolDefinitionDTO;
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

  public PagedEntityCollection<SightEvent> getList(SightEventPagedCollectionConfig config) {
    if (config.isCurrentPartner()) {
      config.setPartner(partnerService.findByUserEmail(ctx.getCallerPrincipal().getName()).getId());
    }

    List<SightEvent> sightEvents = getQuery(config).getResultList().stream()
        .sorted(sightEventDatesComparator()).collect(toList());
    return new PagedEntityCollection<>(sightEvents, config);
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
    if (partner == null) {
      partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    }
    dto.generalAdmission = Boolean.TRUE.equals(dto.generalAdmission);
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    dto = helloTicket.addSightEvent(dto, partner.getHptToken());
    SightEvent bo = new SightEvent();
    DtoMapper.copy(dto, bo);
    iService.update(bo, dto.mainImage == null ? null : dto.mainImage.original);
    bo.generateRandomScore();
    bo.setPortal(hpt);

    if (dto.sightId != null) {
      Sight sight = sightService.get(dto.sightId);
      bo.setSight(sight);
    }

    bo.setPartner(partner);
    em.persist(bo);
    logger.log(Logger.Level.INFO, "Saved new sight event: " + bo.getName());
    return bo;
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
    return bo;
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
    return em.createQuery("from SightEvent where id=:id and partner=:partner", SightEvent.class)
        .setParameter("id", id).setParameter("partner", partner).getSingleResult();
  }

  public void deleteForLoggedUser(Long id) {
    getForLoggedUser(id).setActive(false);
  }


  public void fetchTicketPoolDefinitions(Collection<SightEvent> bos,
      List<SightEventDTO> sightEventDtos) {

    if (hasAnyHptCloudEvent(bos)) {
      var pairedByIds = pairBosWithDtos(bos, sightEventDtos);
      var groupedByPartner = groupByPartner(pairedByIds);
      HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
      Map<Long, TicketDefinition> externalIdToTicket = null;
      for (var entry : groupedByPartner.entrySet()) {
        Partner partner = entry.getKey();
        List<Pair<Long, SightEventDTO>> sightEvents = entry.getValue();
        List<TicketPoolDefinitionDTO> poolDefinitions =
            hpt.getTicketPoolDefinitions(partner.getHptToken());
        List<TicketDefinitionDTO> ticketDefinitions = new ArrayList<>();
        poolDefinitions.forEach(p -> ticketDefinitions.addAll(p.ticketDefinitions));
        List<TicketDefinition> ticketBos = ticketService.getTicketsByExternalIds(
            ticketDefinitions.stream().map(t -> t.id).collect(Collectors.toList()));
        externalIdToTicket =
            ticketBos.stream().collect(Collectors.toMap(TicketDefinition::getExternalId, t -> t));


        var poolDefinitionsGroupedBySightEventId =
            poolDefinitions.stream().collect(Collectors.groupingBy(pool -> pool.sightEventId));
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
              t.id = externalIdToTicket.get(t.id).getId();
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

}
