package pl.hellopoland.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.model.Facility;
import pl.hellopoland.model.Image;

@Startup
@Singleton
public class DbFiller extends ServiceSuperclass {

  @Inject
  ImageService iService;


  @PostConstruct
  public void fillDb() {
    String hibernateStrategy = properties.get("hibernate.hbm2ddl.auto").toString();
    if (!hibernateStrategy.startsWith("create")) {
      logger.info("omitting dbfiller because hibernate.hbm2ddl.auto isnt set to create");
      return;
    }
    Image zooImage = iService.storeImage(getClass().getResourceAsStream("/zoo.png"), ".png");
    Image kolejkowoImage =
        iService.storeImage(getClass().getResourceAsStream("/kolejkowo.png"), ".png");
    Image hydropolisImage =
        iService.storeImage(getClass().getResourceAsStream("/hydropolis.png"), ".png");

    Facility zoo = new Facility();
    zoo.setName("ZOO Wrocław");
    zoo.setMainImage(zooImage);
    em.persist(zoo);

    Facility kolejkowo = new Facility();
    kolejkowo.setName("Kolejkowo");
    kolejkowo.setMainImage(kolejkowoImage);
    em.persist(kolejkowo);

    Facility hydropolis = new Facility();
    hydropolis.setName("Hydropolis");
    hydropolis.setMainImage(hydropolisImage);
    em.persist(hydropolis);

    logger.info("dbfiller finished");
  }
}
