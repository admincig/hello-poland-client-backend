package pl.hellopoland.service;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.dto.FiltersContainerDTO;

@LocalBean
@Stateless
public class FilterService extends ServiceSuperclass {

  public FiltersContainerDTO getForSights() {
    var dto = new FiltersContainerDTO();
    dto.cities = getAllCitiesFromSights();
    return dto;
  }

  public FiltersContainerDTO getForSightEvents() {
    var dto = new FiltersContainerDTO();
    dto.cities = getAllCitiesFromSightEvents();
    return dto;
  }

  private List<String> getAllCitiesFromSights() {
    return em.createQuery("select distinct s.location.city from Sight s " + "where s.active = true "
        + "and s.published = true " + "and s.blocked = false " + "and s.location is not null "
        + "and (s.location.city::char(1) = '') is false", String.class).getResultList();
  }

  private List<String> getAllCitiesFromSightEvents() {
    return em.createQuery(
        "select distinct se.location.city from SightEvent se " + "where se.active = true "
            + "and se.published = true " + "and se.blocked = false "
            + "and se.location is not null " + "and (se.location.city::char(1) = '') is false",
        String.class).getResultList();
  }

}
