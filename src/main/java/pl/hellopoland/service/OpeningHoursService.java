package pl.hellopoland.service;

import pl.hellopoland.bo.OpeningHours;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Collection;
import java.util.Optional;

@LocalBean
@Stateless
public class OpeningHoursService extends ServiceSuperclass {

  public void persist(OpeningHours openingHours) {
    em.persist(openingHours);
  }

  public void remove(Collection<OpeningHours> openingHours) {
    Optional.ofNullable(openingHours).ifPresent(list -> list.stream().forEach(oh -> em.remove(oh)));
  }

}
