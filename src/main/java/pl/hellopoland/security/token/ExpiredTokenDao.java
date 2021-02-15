package pl.hellopoland.security.token;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
@LocalBean
public class ExpiredTokenDao {

  @PersistenceContext
  private EntityManager entityManager;

  public ExpiredToken persist(ExpiredToken token) {
    entityManager.persist(token);

    return token;
  }

  public Optional<ExpiredToken> findByToken(String token) {
    return entityManager
        .createQuery("from ExpiredToken expiredToken where expiredToken.token=:token",
            ExpiredToken.class)
        .setParameter("token", token).getResultStream().findFirst();
  }

}
