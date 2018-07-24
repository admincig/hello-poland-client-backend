package pl.hellopoland.security.token;

import static java.util.stream.Collectors.joining;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String JWT_ACCESS_TOKEN_VALIDITY_PROPERTY =
      "jwt.access.token.validity.millis";
  private static final String JWT_REFRESH_TOKEN_VALIDITY_PROPERTY =
      "jwt.refresh.token.validity.millis";
  private static final String JWT_ACCESS_TOKEN_SECRET_KEY_PROPERTY = "jwt.access.token.secret.key";
  private static final String JWT_REFRESH_TOKEN_SECRET_KEY_PROPERTY =
      "jwt.refresh.token.secret.key";

  public String createToken(String username, Set<String> authorities, TokenType tokenType) {
    if (tokenType == TokenType.ACCESS_TOKEN) {
      return createAccessToken(username, authorities);
    } else {
      return createRefreshToken(username, authorities);
    }
  }

  public JwtCredential getCredential(String token, TokenType tokenType) {
    if (tokenType == TokenType.ACCESS_TOKEN) {
      return getAccessTokenCredential(token);
    } else {
      return getRefreshTokenCredential(token);
    }
  }

  public void validateToken(String authToken, TokenType tokenType) {
    if (tokenType == TokenType.ACCESS_TOKEN) {
      validateAccessToken(authToken);
    } else {
      validateRefreshToken(authToken);
    }
  }

  private String createAccessToken(String username, Set<String> authorities) {
    long now = (new Date()).getTime();
    long accessTokenValidity = Long.valueOf(System.getProperty(JWT_ACCESS_TOKEN_VALIDITY_PROPERTY));
    String accessTokenSecretKey = System.getProperty(JWT_ACCESS_TOKEN_SECRET_KEY_PROPERTY);

    return Jwts.builder().setSubject(username)
        .claim(AUTHORITIES_KEY, authorities.stream().collect(joining(",")))
        .signWith(SignatureAlgorithm.HS512, accessTokenSecretKey)
        .setExpiration(new Date(now + accessTokenValidity)).compact();
  }

  private String createRefreshToken(String username, Set<String> authorities) {
    long now = (new Date()).getTime();
    long refreshTokenValidity =
        Long.valueOf(System.getProperty(JWT_REFRESH_TOKEN_VALIDITY_PROPERTY));
    String refreshTokenSecretKey = System.getProperty(JWT_REFRESH_TOKEN_SECRET_KEY_PROPERTY);

    return Jwts.builder().setSubject(username)
        .claim(AUTHORITIES_KEY, authorities.stream().collect(joining(",")))
        .signWith(SignatureAlgorithm.HS512, refreshTokenSecretKey)
        .setExpiration(new Date(now + refreshTokenValidity)).compact();
  }

  private void validateAccessToken(String token) {
    String accessTokenSecretKey = System.getProperty(JWT_ACCESS_TOKEN_SECRET_KEY_PROPERTY);

    Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(token);

  }

  private void validateRefreshToken(String token) {
    String refreshTokenSecretKey = System.getProperty(JWT_REFRESH_TOKEN_SECRET_KEY_PROPERTY);

    Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(token);
  }

  private JwtCredential getAccessTokenCredential(String token) {
    String accessTokenSecretKey = System.getProperty(JWT_ACCESS_TOKEN_SECRET_KEY_PROPERTY);

    return getCredential(token, accessTokenSecretKey);
  }

  private JwtCredential getRefreshTokenCredential(String token) {
    String refreshTokenSecretKey = System.getProperty(JWT_REFRESH_TOKEN_SECRET_KEY_PROPERTY);

    return getCredential(token, refreshTokenSecretKey);
  }

  private JwtCredential getCredential(String token, String secretKey) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

    Set<String> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        .collect(Collectors.toSet());

    return new JwtCredential(claims.getSubject(), authorities);
  }

}
