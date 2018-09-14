package pl.hellopoland.service;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.bo.Agreement;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.util.DtoMapper;

@LocalBean
@Stateless
public class AgreementService extends ServiceSuperclass {

  public Agreement getForLoggedUser(Long id) {
    return em.createQuery("from Agreement where id=:id and partner=:partner", Agreement.class)
        .setParameter("id", id).setParameter("partner", getLoggedPartner()).getSingleResult();
  }

  public List<Agreement> getForPartner() {
    return em.createQuery("from Agreement where partner=:partner", Agreement.class)
        .setParameter("partner", getLoggedPartner()).getResultList();
  }

  public Agreement create(AgreementDTO dto) {
    Agreement bo = new Agreement();
    bo.setPartner(getLoggedPartner());
    DtoMapper.copy(dto, bo);
    em.persist(bo);
    return bo;
  }

  public void deleteForLoggedUser(Long id) {
    em.remove(getForLoggedUser(id));
  }

  public Sight update(AgreementDTO dto) {
    // TODO Auto-generated method stub
    return null;
  }

}
