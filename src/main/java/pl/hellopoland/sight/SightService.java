package pl.hellopoland.sight;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.dto.SightEventDefinition;
import pl.hellopoland.exception.ExceptionFactory;
import pl.hellopoland.image.Image;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.partner.Partner;
import pl.hellopoland.partner.PartnerService;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.util.HplMapper;

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

  public Sight create(pl.hellopoland.dto.Sight dto, Partner partner) {
    Sight bo = new Sight();
    HplMapper.copy(dto, bo);
    bo.setPartner(partner);
    if (dto.mainImage != null) {
      Image im = imageService.downloadImage(dto.mainImage.original);
      bo.setMainImage(im);
    }
    if (dto.generalAdmission) {
      createGeneralAdmissionSightEvent(bo, partner);
    }
    em.persist(bo);
    return bo;
  }

  public Sight get(Long id) {
    return em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", id).getSingleResult();
  }

  public List<Sight> getActive() {
    return em
        .createQuery("from Sight sight where sight.active=true order by sight.id desc", Sight.class)
        .getResultList();
  }

  public List<Sight> getForPartner(CurrentUser currentUser) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());

    return em.createQuery("from Sight sight where sight.partner=:partner order by sight.id desc",
        Sight.class).setParameter("partner", partner).getResultList();
  }

  public Sight update(Long id, pl.hellopoland.dto.Sight dto) {
    Sight bo = get(id);
    HplMapper.copy(dto, bo);
    return bo;
  }

  public void delete(Long sightId) {
    Sight bo = get(sightId);

    if (hasActiveSightEvents(bo.getSightEvents())) {
      throw exceptionFactory.sightHasAssignedSightEventsException();
    } else {
      bo.setActive(false);
    }
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
    SightEventDefinition sed = HplMapper.getGAEventDTO(sight);
    sightEventService.create(sed, partner);
  }

}
