package pl.hellopoland.service;

import pl.hellopoland.bo.HplInstance;

import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class DiscoveryService extends ServiceSuperclass {

  public List<HplInstance> discovery() {
    return em.createQuery("from HplInstance order by id asc", HplInstance.class).getResultList();
  }

}
