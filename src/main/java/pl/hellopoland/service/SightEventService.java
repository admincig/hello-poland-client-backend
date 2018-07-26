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
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.dto.PushDTO;
import pl.hellopoland.dto.SightEventDTO;
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

  public PagedEntityCollection<SightEvent> getList(SightEventPagedCollectionConfig config) {
    if (config.isCurrentPartner()) {
      config.setPartner(partnerService.findByUserEmail(ctx.getCallerPrincipal().getName()).getId());
    }

    List<SightEvent> sightEvents = getQuery(config).getResultList().stream()
        .sorted(sightEventDatesComparator()).collect(toList());
    return new PagedEntityCollection<>(sightEvents, config);
  }

  public SightEvent get(Long id) {
    SightEvent s = em.find(SightEvent.class, id);

    // fetch collections
    s.getTickets().size();
    s.getOpeningHours().size();
    s.getAgreements().size();

    return s;
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
    iService.update(bo, dto.mainImage == null ? null : dto.mainImage.original);
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
    ByteArrayInputStream is = new ByteArrayInputStream(icon);
    ImageCollector image = iService.validateAndStoreImageCollector(is, "jpeg", null);
    SightEvent bo = get(id);
    get(id).setMainImage(image);
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


  public void fetchTicketPoolDefinitions(Collection<SightEvent> bos, List<SightEventDTO> dtos) {
    if (hasAnyHptCloudEvent(bos)) {
      var pairedByIds = pairBosWithDtos(bos, dtos);
      var groupedByPartner = groupByPartner(pairedByIds);
      HelloTicket hpt = new HelloTicket(getPortal("Hello Ticket Cloud").getUrl());
      groupedByPartner.forEach((partner, sightEvents) -> {
        List<TicketPoolDefinitionDTO> poolDefinitions =
            hpt.getTicketPoolDefinitions(partner.getHptToken());
        var poolDefinitionsGroupedBySightEventId =
            poolDefinitions.stream().collect(Collectors.groupingBy(pool -> pool.sightEventId));
        for (var pair : sightEvents) {
          pair.getRight().ticketPoolDefinitions =
              poolDefinitionsGroupedBySightEventId.get(pair.getLeft());
        }
      });
      dtos.forEach(dto -> {
        dto.ticketPoolDefinitions.forEach(p -> {
          p.sightEventId = dto.id;
        });
      });
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
