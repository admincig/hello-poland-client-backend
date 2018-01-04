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
}
