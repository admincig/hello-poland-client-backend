package pl.hellopoland.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.System.Logger;
import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.security.enterprise.SecurityContext;
import pl.hellopoland.bo.ModelSuperclass;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.User;
import pl.hellopoland.config.PagedCollectionConfig;
import pl.hellopoland.config.PagedCollectionConfig.Entry;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;


public abstract class ServiceSuperclass {

  private static Context namingContext;
  protected static Properties properties;
  private static Logger staticLogger = System.getLogger(ServiceSuperclass.class.getName());
  protected Logger logger = System.getLogger(this.getClass().getName());

  static {
    try {
      namingContext = new InitialContext();
    } catch (NamingException e) {
      staticLogger.log(Logger.Level.WARNING, "Failed to get lookup context", e);
    }

    try {
      properties = System.getProperties();
      var copy = new HashMap<>(properties);
      properties.clear();
      properties.load(ServiceSuperclass.class.getResourceAsStream("/etc/config.properties"));
      properties.load(ServiceSuperclass.class
          .getResourceAsStream("/etc/" + copy.get("user.name") + ".config.properties"));
      if (copy.containsKey("local.properties")) {
        properties.load(new FileInputStream(new File((String) copy.get("local.properties"))));
      }
      properties.putAll(copy);
    } catch (IOException e) {
      staticLogger.log(Logger.Level.WARNING, "Failed to load properties", e);
    }
  }


  @Inject
  protected SecurityContext ctx;
  @PersistenceContext
  protected EntityManager em;

  // @Inject
  // private JMSContext jms;
  // protected boolean sendMessage(String queue, Serializable message) {
  // Queue q = (Queue) lookup(queue);
  // if (q == null) {
  // return false;
  // } else {
  // JMSProducer producer = jms.createProducer();
  // producer.setDeliveryMode(DeliveryMode.PERSISTENT);
  // producer.send(q, message);
  // return false;
  // }
  // }

  protected Object lookup(String jndiName) {
    try {
      return namingContext.lookup(jndiName);
    } catch (NamingException e) {
      logger.log(Logger.Level.WARNING, "Failed to lookup " + jndiName, e);
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
    logger.log(Logger.Level.INFO, humanReadable(query, config.getConditions()));
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

  public Portal getPortal(String name) {
    return em.createQuery("from Portal where name=:name", Portal.class).setParameter("name", name)
        .getSingleResult();
  }

  public User getLoggedUser() {
    try {
      String login = ctx.getCallerPrincipal().getName();
      return em.createQuery("from User where lower(email) = :email", User.class)
          .setParameter("email", login.toLowerCase()).getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public Partner getLoggedPartner() {
    return getLoggedUser().getPartner();
  }

  protected <T extends ModelSuperclass> Comparator<T> getNamesComparator(
      Function<T, String> function, Locale locale) {
    var collator = Collator.getInstance(locale);
    collator.setStrength(Collator.CANONICAL_DECOMPOSITION);
    return Comparator.comparing(function, collator);
  }

  @AroundInvoke
  public Object catchNoResultException(InvocationContext ctx) throws Exception {
    try {
      return ctx.proceed();
    } catch (NoResultException e) {
      throw new ResourceNotFoundException();
    }
  }

}
