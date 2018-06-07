package pl.hellopoland.sight;

import java.lang.System.Logger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.ServiceSuperclass;
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.image.Image;
import pl.hellopoland.image.ImageService;
import pl.hellopoland.util.PagedEntityCollection;
import pl.hellopoland.util.Woo;

@LocalBean
@Stateless
public class SightService extends ServiceSuperclass {

  @Inject
  ImageService iService;

  @PermitAll
  public PagedEntityCollection<Sight> getList(SightsPagedCollectionConfig config) {
    return new PagedEntityCollection<>(getQuery(config).getResultList(), config);
  }

  @PermitAll
  public Sight get(Long id) {
    Sight s = em.find(Sight.class, id);

    // fetch collections
    s.getTickets().size();
    s.getOpeningHours().size();
    s.getAgreements().size();

    return s;
  }

  @PermitAll
  public void runImporter() {
    List<Portal> portals =
        em.createQuery("from Portal where type=:type order by id asc", Portal.class)
            .setParameter("type", Portal.Type.WOOCOMMERCE).getResultList();
    for (Portal portal : portals) {
      logger.log(Logger.Level.INFO, "Importing sights from " + portal.getName());
      Woo woo = new Woo(portal.getUrl(), portal.getKey(), portal.getSecret());
      List<Sight> sights = woo.importSights();
      for (Sight s : sights) {
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
          t.setSight(s);
          em.persist(t);
        });
      }
      logger.log(Logger.Level.INFO, "Finished import of " + portal.getName());
    }
    logger.log(Logger.Level.INFO, "Finished all imports");
  }

  @PermitAll
  public void savePush(Collection<pl.hellopoland.dto.SightEventDefinition> sights) {
    Portal hpt = getPortal("Hello Ticket Cloud");
    sights.forEach(sdto -> {
      var sbo = new Sight();
      sbo.setName(sdto.name);
      sbo.generateRandomScore();
      sbo.setMainImage(iService.downloadImage(sdto.mainImageUrl));
      sbo.setPortal(hpt);

      em.persist(sbo);
      logger.log(Logger.Level.INFO, "Saved new sight: " + sbo.getName());

      sdto.tickets.forEach(tdto -> {
        var tbo = new Ticket();
        tbo.setSight(sbo);
        tbo.setExternalId(tdto.id);
        tbo.setName(tdto.name);
        tbo.setPredefinedDate(tdto.predefinedDate);
        tbo.setPrice(tdto.price);
        em.persist(tbo);
        logger.log(Logger.Level.INFO, "Saved new ticket: " + sbo.getName() + ": " + tbo.getName());
      });
      if (sdto.tickets != null) {
        em.refresh(sbo);
        sbo.setMinPrice(sbo.getTickets().stream().mapToInt(Ticket::getPrice).min().orElse(0));
      }
    });
  }

  private Portal getPortal(String name) {
    return em.createQuery("from Portal where name=:name", Portal.class).setParameter("name", name)
        .getSingleResult();
  }
}
