package pl.hellopoland.sight;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
        em.createQuery("from Portal order by id asc", Portal.class).getResultList();
    for (Portal portal : portals) {
      logger.info("Importing sights from " + portal.getName());
      Woo woo = new Woo(portal.getUrl(), portal.getKey(), portal.getSecret());
      List<Sight> sights = woo.importSights();
      Random random = new Random();
      for (Sight s : sights) {
        logger.info(s.getName());
        boolean anyTicketInFuture = s.getTickets().stream()
            .anyMatch(t -> t.getDate() != null && t.getDate().after(new Date()));
        if (!anyTicketInFuture) {
          logger.info("Omitting. No events in future");
          continue;
        }
        s.setPortal(portal);
        s.setScore((float) (4.8 + random.nextDouble() / 5));
        Image im = s.getMainImage();
        try {
          logger.info("Downloading image " + im.getImageURL());
          im = iService.storeImage(new URL(im.getImageURL()).openConnection().getInputStream(),
              "jpg");
          s.setMainImage(im);
        } catch (Exception e) {
          logger.warning(e.getMessage());
          s.setMainImage(null);
        }
        Collection<Ticket> tickets = s.getTickets();
        em.persist(s);
        tickets.forEach(t -> {
          t.setSight(s);
          em.persist(t);
        });
      }
      logger.info("Finished import of " + portal.getName());
    }
    logger.info("Finished all imports");
  }
}
