package pl.hellopoland.service;

import pl.hellopoland.bo.Agreement;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Set;

@LocalBean
@Stateless
public class AgreementService extends ServiceSuperclass {

  public List<Agreement> getForLoggedUser(Set<Long> ids) {
    return em.createQuery("from Agreement where partner=:partner and id in (:ids)", Agreement.class)
        .setParameter("partner", getLoggedPartner()).setParameter("ids", ids).getResultList();
  }

}
