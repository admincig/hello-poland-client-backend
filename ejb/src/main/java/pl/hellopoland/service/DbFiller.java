package pl.hellopoland.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.model.Image;
import pl.hellopoland.model.Sight;

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
    Image zooImage = iService.storeImage(getClass().getResourceAsStream("/zoo.jpg"), ".jpg");
    Image kolejkowoImage =
        iService.storeImage(getClass().getResourceAsStream("/kolejkowo.jpg"), ".jpg");
    Image hydropolisImage =
        iService.storeImage(getClass().getResourceAsStream("/hydropolis.jpg"), ".jpg");

    Sight zoo = new Sight();
    zoo.setName("ZOO Wrocław");
    zoo.setMainImage(zooImage);
    em.persist(zoo);

    Sight kolejkowo = new Sight();
    kolejkowo.setName("Kolejkowo");
    kolejkowo.setMainImage(kolejkowoImage);
    em.persist(kolejkowo);

    Sight hydropolis = new Sight();
    hydropolis.setName("Hydropolis");
    hydropolis.setMainImage(hydropolisImage);
    em.persist(hydropolis);

    logger.info("dbfiller finished");
  }
}
