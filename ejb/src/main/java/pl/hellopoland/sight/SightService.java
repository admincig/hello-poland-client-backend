package pl.hellopoland.sight;

import java.net.URL;
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
    Woo woo = new Woo("http://woo.hello-poland.pl", "ck_5233b79180ff8b7bef81b28fe7222b2eb2b37ebe",
        "cs_2c96f574d729e8bde7b71d96007c172bc12244d9");
    List<Sight> sights = woo.importSights();
    for (Sight s : sights) {
      Image im = s.getMainImage();
      try {
        im = iService.storeImage(new URL(im.getImageURL()).openConnection().getInputStream(),
            "jpg");
        s.setMainImage(im);
      } catch (Exception e) {
        logger.warning(e.getMessage());
        s.setMainImage(null);
      }
      em.persist(s);
      Ticket t = new Ticket();
      t.setName("Bilet");
      t.setSight(s);
      t.setPrice(s.getMinPrice());
      em.persist(t);
    }
  }
}
