package pl.hellopoland.service;

import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.config.FacilityPagedCollectionConfig;
import pl.hellopoland.config.PagedEntityCollection;
import pl.hellopoland.model.Facility;

@LocalBean
@Stateless
public class FacilityService extends ServiceSuperclass {

  @PermitAll
  public PagedEntityCollection<Facility> getList(FacilityPagedCollectionConfig config) {
    return new PagedEntityCollection<>(getQuery(config).getResultList(), config);
  }
}
