package pl.hellopoland.security.token;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

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
