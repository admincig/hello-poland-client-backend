package pl.hellopoland.sight;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.dto.Push;
import pl.hellopoland.image.Image;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.partner.Partner;
import pl.hellopoland.partner.PartnerService;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.HplMapper;
import pl.hellopoland.util.PagedEntityCollection;
import pl.hellopoland.util.Woo;

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
  private CurrentUser currentUser;

  public PagedEntityCollection<SightEvent> getList(SightsPagedCollectionConfig config) {
    return new PagedEntityCollection<>(getQuery(config).getResultList(), config);
  }

  public SightEvent get(Long id) {
    SightEvent s = em.find(SightEvent.class, id);

    // fetch collections
    s.getTickets().size();
    s.getOpeningHours().size();
    s.getAgreements().size();

    return s;
  }

  public void runWooCommerceImporter() {
    List<Portal> portals =
        em.createQuery("from Portal where type=:type order by id asc", Portal.class)
            .setParameter("type", Portal.Type.WOOCOMMERCE).getResultList();
    for (Portal portal : portals) {
      logger.log(Logger.Level.INFO, "Importing sightEvents from " + portal.getName());
      Woo woo = new Woo(portal.getUrl(), portal.getKey(), portal.getSecret());
      List<SightEvent> sightEvents = woo.importSights();
      for (SightEvent s : sightEvents) {
        logger.log(Logger.Level.INFO, s.getName());
        boolean anyTicketInFuture = s.getTickets().stream()
            .anyMatch(t -> t.getDate() != null && t.getDate().after(new Date()));
        if (!anyTicketInFuture) {
          logger.log(Logger.Level.INFO, "Omitting. No events in future");
          continue;
        }
        s.setPortal(portal);
        s.generateRandomScore();
        Image im = s.getMainImage();
        s.setMainImage(iService.downloadImage(im.getImageURL()));
        Collection<Ticket> tickets = s.getTickets();
        em.persist(s);
        tickets.forEach(t -> {
          t.setSightEvent(s);
          em.persist(t);
        });
      }
      logger.log(Logger.Level.INFO, "Finished import of " + portal.getName());
    }
    logger.log(Logger.Level.INFO, "Finished all imports");
  }

  public void savePush(Push push) {
    Partner partner = partnerService.findByToken(push.secret);
    push.sightEvents.forEach(sdto -> {
      create(sdto, partner);
    });
  }


  public void delete(Long sightEventId) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());

    SightEvent sightEvent =
        em.createQuery("from SightEvent sightEvent where sightEvent.id=:sightEventId",
            SightEvent.class).setParameter("sightEventId", sightEventId).getSingleResult();

    helloTicket.deleteSightEvent(sightEvent, partner.getHptToken());

    sightEvent.setActive(false);
  }

  public SightEvent create(pl.hellopoland.dto.SightEventDefinition dto, Partner partner) {
    if (partner == null) {
      partner = partnerService.findByUserEmail(currentUser.getEmail());
    }
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());
    dto = helloTicket.addSightEvent(dto, partner.getHptToken());

    SightEvent bo = new SightEvent();
    HplMapper.copy(dto, bo);
    iService.update(bo, dto.mainImage.original);
    bo.generateRandomScore();
    bo.setPortal(hpt);

    if (dto.sightId != null) {
      Sight sight = sightService.get(dto.sightId);
      bo.setSight(sight);
    }

    bo.setPartner(partner);
    em.persist(bo);
    logger.log(Logger.Level.INFO, "Saved new sight: " + bo.getName());
    return bo;
  }


  private Portal getPortal(String name) {
    return em.createQuery("from Portal where name=:name", Portal.class).setParameter("name", name)
        .getSingleResult();
  }

  public SightEvent update(Long id, pl.hellopoland.dto.SightEventDefinition dto) {
    SightEvent bo = get(id);
    if (bo.getPortal().getType() == Portal.Type.HELLOTICKET_CLOUD_1) {
      Partner partner = partnerService.findByUserEmail(currentUser.getEmail());
      Portal hpt = getPortal("Hello Ticket Cloud");
      HelloTicket helloTicket = new HelloTicket(hpt.getUrl());

      dto.id = bo.getHptId();
      dto = helloTicket.updateSightEvent(dto, partner.getHptToken());
    }
    HplMapper.copy(dto, bo);
    iService.update(bo, dto.mainImage.original);
    return bo;
  }

}
