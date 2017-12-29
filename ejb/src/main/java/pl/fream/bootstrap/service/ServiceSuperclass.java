package pl.fream.bootstrap.service;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class ServiceSuperclass {

  private static Context namingContext;
  static {
    try {
      namingContext = new InitialContext();
    } catch (NamingException e) {
      Logger.getAnonymousLogger().log(Level.WARNING, "Failed to get lookup context", e);
    }
  }

  @PersistenceContext
  protected EntityManager em;

  protected Logger logger = Logger.getLogger(getClass().getName());


  @Inject
  private JMSContext jms;

  protected boolean sendMessage(String queue, Serializable message) {
    Queue q = (Queue) lookup(queue);
    if (q == null) {
      return false;
    } else {
      jms.createProducer().send(q, message);
      return false;
    }
  }

  protected Object lookup(String jndiName) {
    try {
      return namingContext.lookup(jndiName);
    } catch (NamingException e) {
      logger.log(Level.WARNING, "Failed to lookup " + jndiName, e);
      return null;
    }
  }


}
