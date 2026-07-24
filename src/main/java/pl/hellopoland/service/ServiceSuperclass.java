package pl.hellopoland.service;

import pl.hellopoland.bo.ModelSuperclass;
import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Portal;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.HptSubject;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.config.Entry;
import pl.hellopoland.config.PagedCollectionConfig;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.exception.notfound.ResourceNotFoundException;

import org.apache.commons.lang3.StringUtils;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.SecurityContext;
import java.io.*;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.text.Collator;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public abstract class ServiceSuperclass {

  protected static Properties properties;
  private static Logger staticLogger = System.getLogger(ServiceSuperclass.class.getName());

  static {
    properties = System.getProperties();
    try {
      try (InputStream is = ServiceSuperclass.class.getResourceAsStream("/runtime.properties")) {
        Reader reader = new InputStreamReader(is, "UTF-8");
        properties.load(reader);
      }
      if (properties.containsKey("local.runtime.properties")) {
        try (FileInputStream fis = new FileInputStream(new File((String) properties.get("local.runtime.properties")))) {
          Reader reader = new InputStreamReader(fis, "UTF-8");
          properties.load(reader);
        }
      }
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
      config.getConditions()
          .forEach(condition -> {
            if (condition.parameters != null) {
              condition.parameters.forEach(tq::setParameter);
            } else {
              tq.setParameter(condition.parameterName, condition.value);
            }
          });
    }
    if (config.getPageSize() != null) {
      tq.setMaxResults(config.getPageSize());
      tq.setFirstResult(config.getPageSize() * config.getPageNum());
    }
    logger.log(Logger.Level.INFO, humanReadable(query, config.getConditions()));
    return tq;
  }

  private String humanReadable(String query,
      Collection<Entry> collection) {
    if (collection != null) {
      for (var e : collection) {
        if (e.parameters != null) {
          for (var parameter : e.parameters.entrySet()) {
            query = query.replaceAll(":" + parameter.getKey(), parameter.getValue().toString());
          }
        } else {
          query = query.replaceAll(":" + e.parameterName, e.value.toString());
        }
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

  public String getHelpdeskHptToken() {
    String configuredToken = properties.getProperty("hpt.helpdesk.token");
    if (StringUtils.isNotBlank(configuredToken)) {
      return configuredToken.trim();
    }

    return em.createQuery(
            "select distinct u from User u join u.roles r "
                + "where u.deleted = false and u.blocked = false and u.hptToken is not null "
                + "and r.role in (:roles)",
            User.class)
        .setParameter("roles", List.of(Role.ROOT, Role.ADMIN))
        .getResultStream()
        .filter(user -> StringUtils.isNotBlank(user.getHptToken()))
        .sorted(Comparator.comparing((User user) -> !user.hasRole(Role.ROOT))
            .thenComparing(User::getId))
        .map(user -> user.getHptToken().trim())
        .findFirst()
        .orElseThrow(() -> new ConflictingException(
            "Brak skonfigurowanego tokena administracyjnego HelloTicket."));
  }

  public HptSubject getHelpdeskHptSubject() {
    return this::getHelpdeskHptToken;
  }

  protected boolean hasHelpdeskSystemRole(User user) {
    return user != null && (user.hasRole(Role.ROOT) || user.hasRole(Role.ADMIN)
        || user.hasRole(Role.SALESMAN) || user.hasRole(Role.HELPDESK_PARTNER_MANAGER)
        || user.hasRole(Role.HELPDESK_CONTENT_MANAGER) || user.hasRole(Role.HELPDESK_SUPPORT));
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
