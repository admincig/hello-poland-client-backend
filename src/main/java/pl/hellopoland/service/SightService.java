package pl.hellopoland.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.ImageCollector;
import pl.hellopoland.bo.OpeningHours;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.dto.SightDTO;
import pl.hellopoland.dto.SightEventDTO;
import pl.hellopoland.exception.ExceptionFactory;
import pl.hellopoland.util.DtoMapper;
import pl.hellopoland.util.PagedEntityCollection;

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

  public PagedEntityCollection<Sight> getList(SightPagedCollectionConfig config) {
    if (config.isCurrentPartner()) {
      config.setPartner(partnerService.findByUserEmail(ctx.getCallerPrincipal().getName()).getId());
    }
    config.setOrderColumn("name");
    config.setOrderDirection("asc");
    List<Sight> sight = getQuery(config).getResultList();
    return new PagedEntityCollection<>(sight, config);
  }

  public Sight create(SightDTO dto, Partner partner) {
    Sight bo = new Sight();
    DtoMapper.copy(dto, bo);
    if (partner == null) {
      partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());
    }
    bo.generateRandomScore();
    bo.setPartner(partner);
    imageService.update(bo, dto.mainImage == null ? null : dto.mainImage.original);
    em.persist(bo);

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
    return get(bo.getId());
  }

  public Sight get(Long id) {
    Sight bo = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", id).getSingleResult();
    // fetch events
    if (bo.getSightEvents() != null) {
      bo.getSightEvents().forEach(se -> {
        // fetch tickets
        if (se.getTickets() != null) {
          se.getTickets().size();
        }
      });
    }
    return bo;
  }

  public List<Sight> getActiveForPartner() {
    Partner partner = partnerService.findByUserEmail(ctx.getCallerPrincipal().getName());

    return em.createQuery(
        "from Sight sight where sight.active=true and sight.partner=:partner order by sight.id desc",
        Sight.class).setParameter("partner", partner).getResultList();
  }

  public Sight update(Long id, SightDTO dto) {
    Sight bo = get(id);
    DtoMapper.copy(dto, bo);
    return get(id);
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

  public Sight uploadMainImageForLoggedUser(Long id, byte[] icon) {
    Sight bo = getActiveForLoggedUser(id);
    bo.setMainImage(
        imageService.validateAndStoreImageCollector(new ByteArrayInputStream(icon), "jpeg", null));
    return bo;
  }

  public Sight addImageToSightGallery(Long id, byte[] img) {
    Sight bo = getActiveForLoggedUser(id);
    bo.addImage(
        imageService.validateAndStoreImageCollector(new ByteArrayInputStream(img), "jpeg", null));
    return bo;
  }

  public Sight removeImageFromGallery(Long id, Long imgId) {
    Sight bo = getActiveForLoggedUser(id);
    ImageCollector img = imageService.get(imgId);
    bo.removeImage(img);
    return bo;
  }

  public Sight getActiveForLoggedUser(Long id) {
    return em
        .createQuery("from Sight where id=:id and active=true and partner=:partner", Sight.class)
        .setParameter("id", id).setParameter("partner", getLoggedPartner()).getSingleResult();
  }

  public Sight updateForLoggedUser(SightDTO dto) {
    Sight bo = getActiveForLoggedUser(dto.id);
    DtoMapper.copy(dto, bo);

    ArrayList<OpeningHours> oHoursList = getOpeningHoursCollectionFromDTO(dto);
    if (oHoursList != null && !oHoursList.isEmpty()) {
      oHoursList.stream().forEach(oh -> {
        oh.setSight(bo);
        em.persist(oh);
      });
    }

    oHoursService.remove(bo.getOpeningHours());


    // bo.setOpeningHours(null);
    bo.setOpeningHours(oHoursList);
    return getActiveForLoggedUser(dto.id);
  }

  private ArrayList<OpeningHours> getOpeningHoursCollectionFromDTO(SightDTO dto) {
    return Optional.ofNullable(dto.openingHours)
        .map(l -> l.stream().map(oh -> DtoMapper.copy(oh, new OpeningHours()))
            .collect(Collectors.toCollection(ArrayList::new)))
        .orElse(null);
  }

  public void deleteForLoggedUser(Long id) {
    Sight bo = getActiveForLoggedUser(id);
    if (hasActiveSightEvents(bo.getSightEvents())) {
      throw exceptionFactory.sightHasAssignedSightEventsException();
    } else {
      bo.setActive(false);
    }
  }

}
