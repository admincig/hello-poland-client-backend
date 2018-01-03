package pl.hellopoland.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.jms.DeliveryMode;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class ServiceSuperclass {

  private static Context namingContext;
  protected static Properties properties;
  static {
    try {
      namingContext = new InitialContext();
    } catch (NamingException e) {
      Logger.getAnonymousLogger().log(Level.WARNING, "Failed to get lookup context", e);
    }

    try {
      InputStream input = ServiceSuperclass.class.getResourceAsStream("/config.properties");
      properties = new Properties();
      properties.load(input);
    } catch (IOException e) {
      Logger.getAnonymousLogger().log(Level.WARNING, "Failed to load properties", e);
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
      JMSProducer producer = jms.createProducer();
      producer.setDeliveryMode(DeliveryMode.PERSISTENT);
      producer.send(q, message);
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
