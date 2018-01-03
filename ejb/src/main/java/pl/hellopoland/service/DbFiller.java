package pl.hellopoland.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import pl.hellopoland.model.Image;

@Startup
@Singleton
public class DbFiller extends ServiceSuperclass {

  @Inject
  ImageService iService;


  @PostConstruct
  public void fillDb() {
    Image i = iService.testImage();
    logger.info("created test image: " + i.getHash() + i.getExtension());
  }
}
