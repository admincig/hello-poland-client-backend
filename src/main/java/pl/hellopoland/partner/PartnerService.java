package pl.hellopoland.partner;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import pl.hellopoland.ServiceSuperclass;

@LocalBean
@Stateless
public class PartnerService extends ServiceSuperclass {

  public Partner findByUserEmail(String email) {
    return em.createQuery(
        "select partner from User user join user.partner partner where user.email=:email",
        Partner.class)
        .setParameter("email", email)
        .getSingleResult();
  }

  public Partner findByToken(String token) {
    return em.createQuery(
        "select partner from Partner partner where partner.hptToken=:token",
        Partner.class)
        .setParameter("token", token)
        .getSingleResult();
  }
}
