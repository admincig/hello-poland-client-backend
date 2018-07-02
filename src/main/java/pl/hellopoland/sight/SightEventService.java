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
import pl.hellopoland.dto.SightEventDefinition;
import pl.hellopoland.image.Image;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.partner.Partner;
import pl.hellopoland.partner.PartnerService;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.util.HelloTicket;
import pl.hellopoland.util.PagedEntityCollection;
import pl.hellopoland.util.Woo;

@LocalBean
@Stateless
public class SightEventService extends ServiceSuperclass {

  @Inject
  private ImageService iService;

  @Inject
  private PartnerService partnerService;

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

  public void runImporter() {
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
      createSightEvent(sdto, partner);
    });
  }

  public SightEventDefinition addToHpt(SightEventDefinition sightEventDTO,
      CurrentUser currentUser) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());

    Portal hpt = getPortal("Hello Ticket Cloud");

    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());

    SightEventDefinition sightEventAddedToHpt = helloTicket
        .addSightEvent(sightEventDTO, partner.getHptToken());

    createSightEvent(sightEventAddedToHpt, partner);

    return sightEventAddedToHpt;
  }

  public void delete(Long sightEventId, CurrentUser currentUser) {
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());

    SightEvent sightEvent = em
        .createQuery("from SightEvent sightEvent where sightEvent.id=:sightEventId",
            SightEvent.class)
        .setParameter("sightEventId", sightEventId)
        .getSingleResult();

    helloTicket.deleteSightEvent(sightEvent, partner.getHptToken());

    sightEvent.setActive(false);
  }

  public SightEventDefinition updateInHpt(Long sightEventId, SightEventDefinition sightEventDTO,
      CurrentUser currentUser) {
    SightEvent sightEvent = get(sightEventId);
    Partner partner = partnerService.findByUserEmail(currentUser.getEmail());
    Portal hpt = getPortal("Hello Ticket Cloud");
    HelloTicket helloTicket = new HelloTicket(hpt.getUrl());

    sightEventDTO.id = sightEvent.getHptId();

    SightEventDefinition sightEventUpdatedInHpt = helloTicket
        .updateSightEvent(sightEventDTO, partner.getHptToken());

    update(sightEvent, sightEventUpdatedInHpt);

    return sightEventUpdatedInHpt;
  }

  private SightEvent createSightEvent(SightEventDefinition sightEventDTO, Partner partner) {
    Portal hpt = getPortal("Hello Ticket Cloud");
    SightEvent sightEvent = new SightEvent();

    sightEvent.setName(sightEventDTO.name);
    sightEvent.setDate(sightEventDTO.date);
    sightEvent.setAvailableTicketsNumber(sightEventDTO.availableTicketsNumber);
    sightEvent.setMainImage(iService.downloadImage(sightEventDTO.mainImageUrl));
    sightEvent.setPortal(hpt);
    sightEvent.setLead(sightEventDTO.lead);
    sightEvent.setDescription(sightEventDTO.description);
    sightEvent.setEmail(sightEventDTO.email);
    sightEvent.setPhone(sightEventDTO.phone);
    sightEvent.setHptId(sightEventDTO.id);

    if (sightEventDTO.location != null) {
      SightLocation location = new SightLocation();

      location.setLatitude(sightEventDTO.location.latitude);
      location.setLongitude(sightEventDTO.location.longitude);
      location.setStreet(sightEventDTO.location.street);
      location.setZipCode(sightEventDTO.location.zipCode);
      location.setCity(sightEventDTO.location.city);
      location.setCountry(sightEventDTO.location.country);

      sightEvent.setLocation(location);
    }

    sightEvent.generateRandomScore();

    if (sightEventDTO.sightId != null) {
      assignSightToSightEvent(sightEvent, sightEventDTO.sightId);
    }

    sightEvent.setPartner(partner);
    em.persist(sightEvent);
    logger.log(Logger.Level.INFO, "Saved new sight: " + sightEvent.getName());

    return sightEvent;
  }


  private Portal getPortal(String name) {
    return em.createQuery("from Portal where name=:name", Portal.class).setParameter("name", name)
        .getSingleResult();
  }

  private void assignSightToSightEvent(SightEvent sightEvent, Long sightId) {
    Sight sight = em.createQuery("from Sight sight where sight.id=:sightId", Sight.class)
        .setParameter("sightId", sightId)
        .getResultStream()
        .findFirst()
        .get();

    sightEvent.setSight(sight);
  }


  public void update(SightEvent sightEvent, SightEventDefinition sightEventDTO) {
    sightEvent.setName(sightEventDTO.name);
    sightEvent.setDate(sightEventDTO.date);
    sightEvent.setAvailableTicketsNumber(sightEventDTO.availableTicketsNumber);
    sightEvent.setMainImage(iService.downloadImage(sightEventDTO.mainImageUrl));
    sightEvent.setLead(sightEventDTO.lead);
    sightEvent.setDescription(sightEventDTO.description);
    sightEvent.setEmail(sightEventDTO.email);
    sightEvent.setPhone(sightEventDTO.phone);
    sightEvent.setHptId(sightEventDTO.id);
  }
}