package pl.hellopoland.service;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.model.Facility;

@LocalBean
@Stateless
public class FacilityService extends ServiceSuperclass {

  @PermitAll
  public List<Facility> getList() {
    return em.createQuery("from Facility f left join fetch f.mainImage mi order by f.id asc",
        Facility.class).getResultList();
  }
}
