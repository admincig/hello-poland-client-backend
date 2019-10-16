package pl.hellopoland.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.security.enterprise.SecurityContext;
import pl.hellopoland.bo.ModelSuperclass;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.User;
import pl.hellopoland.config.Entry;
import pl.hellopoland.config.PagedCollectionConfig;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;


public abstract class ServiceSuperclass {

  protected static Properties properties;
  private static Logger staticLogger = System.getLogger(ServiceSuperclass.class.getName());

  static {
    try {
      properties = System.getProperties();
      var copy = new HashMap<>(properties);
      properties.clear();
      properties.load(ServiceSuperclass.class.getResourceAsStream("/runtime.properties"));
      if (copy.containsKey("local.runtime.properties")) {
        properties
            .load(new FileInputStream(new File((String) copy.get("local.runtime.properties"))));
      }
      properties.putAll(copy);
      staticLogger.log(Logger.Level.DEBUG,
          properties.entrySet().stream().map(Object::toString).collect(Collectors.joining("\n")));
    } catch (IOException e) {
      staticLogger.log(Logger.Level.WARNING, "Failed to load properties", e);
    }
  }


  @Inject
  protected SecurityContext ctx;
  @PersistenceContext
  protected EntityManager em;

  protected Logger logger = System.getLogger(this.getClass().getName());

  protected <E extends ModelSuperclass> TypedQuery<E> getQuery(PagedCollectionConfig<E> config) {
    String query = "select e from " + config.entityClass().getSimpleName() + " e " + config.joins();
    if (config.getConditions() != null) {
      query += " where ";
      query +=
          config.getConditions().stream().map(Entry::toString).collect(Collectors.joining(" and "));
    }
    query += " order by " + config.getOrder();
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
      Collection<Entry> collection) {
    if (collection != null) {
      for (var e : collection) {
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

  @AroundInvoke
  public Object catchNoResultException(InvocationContext ctx) throws Exception {
    try {
      return ctx.proceed();
    } catch (NoResultException e) {
      logger.log(Level.ERROR, "NoResultException occured ", e);
      throw new ResourceNotFoundException();
    }
  }

  protected <T extends ModelSuperclass> Comparator<T> getNamesComparator(
      Function<T, String> function, Locale locale) {
    var collator = Collator.getInstance(locale);
    collator.setStrength(Collator.CANONICAL_DECOMPOSITION);
    return Comparator.comparing(function, collator);
  }

}
