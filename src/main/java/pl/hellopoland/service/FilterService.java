package pl.hellopoland.service;

import java.util.List;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.dto.FiltersContainerDTO;

@LocalBean
@Stateless
public class FilterService extends ServiceSuperclass {

  public FiltersContainerDTO getForSights() {
    var dto = new FiltersContainerDTO();
    dto.cities = getCitiesFromSights();
    return dto;
  }

  public FiltersContainerDTO getForSightEvents(Set<Long> sightEventsIds) {
    var dto = new FiltersContainerDTO();
    dto.cities = getCitiesFromSightEvents(sightEventsIds);
    return dto;
  }

  private List<String> getCitiesFromSights() {
    return em.createQuery(
        "select distinct trim(s.location.city) from Sight s where s.active = true "
            + "and s.published = true and s.blocked = false and trim(s.location.city) != ''",
        String.class).getResultList();
  }

  private List<String> getCitiesFromSightEvents(Set<Long> sightEventsIds) {
    if (sightEventsIds == null || sightEventsIds.isEmpty()) {
      return em.createQuery("select distinct trim(se.location.city) from SightEvent se "
          + "where se.active = true and se.published = true and se.blocked = false "
          + "and trim(se.location.city) != ''", String.class).getResultList();
    }
    return em
        .createQuery("select distinct trim(se.location.city) from SightEvent se "
            + "where se.id in (:sightEventsIds) and trim(se.location.city) != ''", String.class)
        .setParameter("sightEventsIds", sightEventsIds).getResultList();
  }

}
