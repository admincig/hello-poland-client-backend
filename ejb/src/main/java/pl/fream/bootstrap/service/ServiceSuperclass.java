package pl.fream.bootstrap.service;

import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class ServiceSuperclass {

  @PersistenceContext
  protected EntityManager em;

  protected Logger log = Logger.getLogger(getClass().getName());

}
