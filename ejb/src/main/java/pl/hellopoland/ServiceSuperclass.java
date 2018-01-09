package pl.hellopoland;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javax.persistence.TypedQuery;
import pl.hellopoland.config.PagedCollectionConfig;
import pl.hellopoland.config.PagedCollectionConfig.Entry;

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

  @Inject
  private JMSContext jms;

  @PersistenceContext
  protected EntityManager em;

  protected Logger logger = Logger.getLogger(getClass().getName());

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

  protected <E extends ModelSuperclass> TypedQuery<E> getQuery(PagedCollectionConfig<E> config) {
    String query = "from " + config.entityClass().getSimpleName() + " e " + config.joins();
    if (config.getConditions() != null) {
      query += " where ";
      query +=
          config.getConditions().stream().map(Entry::toString).collect(Collectors.joining(" and "));
    }
    query += " order by e." + config.getOrder();
    TypedQuery<E> tq = em.createQuery(query, config.entityClass());
    if (config.getConditions() != null) {
      config.getConditions().forEach(condition -> {
        tq.setParameter(condition.parameterName, condition.value);
      });
    }
    if (config.getPageSize() != null) {
      tq.setMaxResults(config.getPageSize());
      tq.setFirstResult(config.getPageSize() * config.getPageNum());
    }
    logger.info(humanReadable(query, config.getConditions()));
    return tq;
  }

  private <E extends ModelSuperclass> String humanReadable(String query,
      Collection<PagedCollectionConfig<E>.Entry> conditions) {
    if (conditions != null) {
      for (PagedCollectionConfig<E>.Entry e : conditions) {
        query = query.replaceAll(":" + e.parameterName, e.value.toString());
      }
    }
    return "Executing select query:\n" + query;
  }

}
