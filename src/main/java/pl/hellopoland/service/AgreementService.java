package pl.hellopoland.service;

import java.util.List;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.bo.Agreement;

@LocalBean
@Stateless
public class AgreementService extends ServiceSuperclass {

  public List<Agreement> getForLoggedUser(Set<Long> ids) {
    return em.createQuery("from Agreement where partner=:partner and id in (:ids)", Agreement.class)
        .setParameter("partner", getLoggedPartner()).setParameter("ids", ids).getResultList();
  }

}
