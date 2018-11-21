package pl.hellopoland.service;

import java.util.List;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.hellopoland.bo.Agreement;
import pl.hellopoland.dto.AgreementDTO;
import pl.hellopoland.util.DtoMapper;

@LocalBean
@Stateless
public class AgreementService extends ServiceSuperclass {

  @Inject
  private TranslationService translationService;

  public Agreement getForLoggedUser(Long id) {
    return em.createQuery("from Agreement where id=:id and partner=:partner", Agreement.class)
        .setParameter("id", id).setParameter("partner", getLoggedPartner()).getSingleResult();
  }

  public List<Agreement> getForLoggedUser() {
    return em.createQuery("from Agreement where partner=:partner", Agreement.class)
        .setParameter("partner", getLoggedPartner()).getResultList();
  }

  public List<Agreement> getForLoggedUser(Set<Long> ids) {
    return em.createQuery("from Agreement where partner=:partner and id in (:ids)", Agreement.class)
        .setParameter("partner", getLoggedPartner()).setParameter("ids", ids).getResultList();
  }

  public Agreement create(AgreementDTO dto) {
    Agreement bo = new Agreement();
    bo.setPartner(getLoggedPartner());
    DtoMapper.copy(dto, bo);
    em.persist(bo);
    return bo;
  }

  public Agreement createLanguageVersion(AgreementDTO dto, String language) {
    return translationService.createEntityLanguageVersion(getForLoggedUser(dto.id), dto, language);
  }

  public void deleteForLoggedUser(Long id) {
    em.remove(getForLoggedUser(id));
  }

  public Agreement updateForLoggedUser(AgreementDTO dto) {
    var bo = getForLoggedUser(dto.id);
    em.detach(bo);
    var updated = DtoMapper.copy(dto, bo);
    updated.setId(null);
    em.persist(updated);
    return getForLoggedUser(updated.getId());
  }

  public Agreement updateLanguageVersion(AgreementDTO dto, String language) {
    return translationService.updateEntityLanguageVersion(getForLoggedUser(dto.id), dto, language);
  }

}
