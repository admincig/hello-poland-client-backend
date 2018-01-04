package pl.hellopoland.service;

import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.config.FacilityPagedCollectionConfig;
import pl.hellopoland.model.Facility;
import pl.hellopoland.util.PagedEntityCollection;

@LocalBean
@Stateless
public class FacilityService extends ServiceSuperclass {

  @PermitAll
  public PagedEntityCollection<Facility> getList(FacilityPagedCollectionConfig config) {
    return new PagedEntityCollection<>(getQuery(config).getResultList(), config);
  }
}
