package pl.hellopoland.service;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.bo.Partner;

@LocalBean
@Stateless
public class PartnerService extends ServiceSuperclass {

  public Partner findByUserEmail(String email) {
    return em.createQuery(
        "select partner from User user join user.partner partner where lower(user.email) = :email",
        Partner.class).setParameter("email", email.toLowerCase()).getSingleResult();
  }

  public Partner findByToken(String token) {
    return em.createQuery("select partner from Partner partner where partner.hptToken=:token",
        Partner.class).setParameter("token", token).getSingleResult();
  }

  public List<Partner> getAll() {
    return em.createQuery("from Partner order by id asc", Partner.class).getResultList();
  }
}
