package pl.hellopoland.security.token;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
@LocalBean
public class ExpiredTokenService {

  @Inject
  private ExpiredTokenDao expiredTokenDao;

  public ExpiredToken addTokenToExpiredTokensList(String token) {
    ExpiredToken expiredToken = new ExpiredToken();
    expiredToken.setToken(token);
    return expiredTokenDao.persist(expiredToken);
  }

  public boolean isTokenInExpiredTokensList(String token) {
    return expiredTokenDao.findByToken(token).isPresent();
  }
}
