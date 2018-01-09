package pl.hellopoland.service;

import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.config.SightsPagedCollectionConfig;
import pl.hellopoland.model.Sight;
import pl.hellopoland.util.PagedEntityCollection;

@LocalBean
@Stateless
public class SightService extends ServiceSuperclass {

  @PermitAll
  public PagedEntityCollection<Sight> getList(SightsPagedCollectionConfig config) {
    return new PagedEntityCollection<>(getQuery(config).getResultList(), config);
  }

  @PermitAll
  public Sight get(Long id) {
    return em.createQuery("from Sight s join fetch s.tickets t where s.id=:id", Sight.class)
        .setParameter("id", id).getSingleResult();
  }
}
