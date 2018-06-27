package pl.hellopoland.security;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static javax.security.enterprise.AuthenticationStatus.SEND_FAILURE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.json.stream.JsonParsingException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.CredentialValidationResult.Status;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import pl.hellopoland.security.dto.CurrentUser;
import pl.hellopoland.security.dto.UserAuthDTO;
import pl.hellopoland.security.token.ExpiredTokenService;
import pl.hellopoland.security.token.JwtCredential;
import pl.hellopoland.security.token.TokenInExpiredTokensListException;
import pl.hellopoland.security.token.TokenProvider;
import pl.hellopoland.security.token.TokenType;
import pl.hellopoland.user.User;
import pl.hellopoland.user.UserRole;
import pl.hellopoland.user.UserService;
import pl.hellopoland.util.FacebookAPIConnector;
import pl.hellopoland.util.GoogleAPIConnector;

@ApplicationScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

  private static Logger staticLogger = System.getLogger(JwtAuthenticationMechanism.class.getName());

  private static final String AUTHORIZATION_PREFIX = "Bearer ";
  private static final String AUTHENTICATION_METHOD = "POST";

  private static final String LOGIN_REQUEST_PATH = "/login";
  private static final String REFRESH_TOKEN_REQUEST_PATH = "/refresh";
  private static final String LOGOUT_REQUEST_PATH = "/logout";

  @Inject
  private IdentityStoreHandler identityStoreHandler;

  @Inject
  private TokenProvider tokenProvider;

  @Inject
  private Event<CurrentUser> authenticatedEvent;

  @Inject
  private ExpiredTokenService expiredTokenService;

  @Inject
  private FacebookAPIConnector facebookAPIConnector;

  @Inject
  private GoogleAPIConnector googleAPIConnector;

  @Inject
  private UserService userService;

  @Override
  public AuthenticationStatus validateRequest(HttpServletRequest request,
      HttpServletResponse response, HttpMessageContext context) {
    AuthenticationStatus authenticationStatus = null;
    staticLogger.log(Level.ERROR, "JWTAuthenticationMechanism validateRequest1");
    String authorizationToken = extractToken(context);
    staticLogger.log(Level.ERROR, "JWTAuthenticationMechanism validateRequest2");

    if (isAuthRequest(request)) {
      staticLogger.log(Level.ERROR, "JWTAuthenticationMechanism validateRequest3");
      Optional<UserAuthDTO> userAuthDTO = extractUserAuthDTO(request);

      String login = userAuthDTO.map(u -> u.login).orElse(null);
      String password = userAuthDTO.map(u -> u.password).orElse(null);

      String socialMediaAuthenticationToken = userAuthDTO.map(u -> u.socialMediaAccessToken)
          .orElse(null);

      String accessToken = userAuthDTO.map(u -> u.accessToken).orElse(null);
      String refreshToken = userAuthDTO.map(u -> u.refreshToken).orElse(null);
      staticLogger.log(Level.ERROR, "JWTAuthenticationMechanism validateRequest4");

      if (isLoginRequest(request)) {
        if (hasProperDataToLogin(login, password)) {
          authenticationStatus = login(login, password, context);
        } else if (hasProperDataToLoginUsingSocialMedia(socialMediaAuthenticationToken)) {
          authenticationStatus = login(socialMediaAuthenticationToken, context);
        } else {
          authenticationStatus = context.responseUnauthorized();
        }
      } else if (isRefreshingRequest(authorizationToken, request)) {
        authenticationStatus = validateRefreshToken(authorizationToken, context);
      } else if (isLogoutRequest(accessToken, refreshToken, request)) {
        authenticationStatus = logout(accessToken, refreshToken, context);
      }
    } else if (authorizationToken != null) {
      staticLogger
          .log(Level.ERROR, "JWTAuthenticationMechanism validateRequest1 {}" + authorizationToken);
      authenticationStatus = validateAccessToken(authorizationToken, context);
    } else if (context.isProtected()) {
      staticLogger.log(Level.ERROR, "JWTAuthenticationMechanism validateRequest12");
      authenticationStatus = context.responseUnauthorized();
    } else {
      staticLogger.log(Level.ERROR,
          "JWTAuthenticationMechanism validateRequest13 " + request.getRequestURI());
      authenticationStatus = context.doNothing();
    }

    return authenticationStatus;
  }

  private Optional<UserAuthDTO> extractUserAuthDTO(HttpServletRequest request) {
    Optional<UserAuthDTO> userAuthDTO = empty();
    String userAuthJson = "";

    try {
      userAuthJson = new BufferedReader(new InputStreamReader(request.getInputStream())).lines()
          .collect(joining("\n"));
    } catch (Exception ignored) {
    }

    if (!userAuthJson.isEmpty()) {
      try {
        userAuthDTO = ofNullable(JsonbBuilder.create().fromJson(userAuthJson, UserAuthDTO.class));
      } catch (JsonParsingException ignored) {
      }
    }

    return userAuthDTO;
  }

  private AuthenticationStatus validateAccessToken(String token, HttpMessageContext context) {
    AuthenticationStatus authenticationStatus;

    try {
      validateTokenNotInExpiredTokensList(token);
      tokenProvider.validateToken(token, TokenType.ACCESS_TOKEN);
      JwtCredential credential = tokenProvider.getCredential(token, TokenType.ACCESS_TOKEN);

      var user = new CurrentUser();
      user.setEmail(credential.getPrincipal());
      user.setRoles(credential.getAuthorities());
      authenticatedEvent.fire(user);

      authenticationStatus =
          context.notifyContainerAboutLogin(credential.getPrincipal(), credential.getAuthorities());

    } catch (Exception e) {
      authenticationStatus = context.responseUnauthorized();
    }

    return authenticationStatus;
  }

  private String extractToken(HttpMessageContext context) {
    String token = null;
    String authorizationHeader = context.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

    if (hasAuthorizationHeader(authorizationHeader)) {
      token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length(),
          authorizationHeader.length());
    }

    return token;
  }

  private boolean hasAuthorizationHeader(String authorizationHeader) {
    return authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_PREFIX);
  }

  private boolean isLoginRequest(HttpServletRequest request) {
    return AUTHENTICATION_METHOD.equals(request.getMethod())
        && request.getRequestURI().endsWith(LOGIN_REQUEST_PATH);
  }

  private boolean hasProperDataToLogin(String email, String password) {
    return email != null && password != null;
  }

  private boolean hasProperDataToLoginUsingSocialMedia(String socialMediaAuthenticationToken) {
    return socialMediaAuthenticationToken != null;
  }


  private boolean isAuthRequest(HttpServletRequest request) {
    return request.getRequestURI().endsWith(LOGIN_REQUEST_PATH)
        || request.getRequestURI().endsWith(REFRESH_TOKEN_REQUEST_PATH)
        || request.getRequestURI().endsWith(LOGOUT_REQUEST_PATH);
  }

  private boolean isRefreshingRequest(String token, HttpServletRequest request) {
    return token != null && AUTHENTICATION_METHOD.equals(request.getMethod())
        && request.getRequestURI().endsWith(REFRESH_TOKEN_REQUEST_PATH);
  }

  private boolean isLogoutRequest(String accessToken, String refreshToken,
      HttpServletRequest request) {
    return accessToken != null && refreshToken != null
        && AUTHENTICATION_METHOD.equals(request.getMethod())
        && request.getRequestURI().endsWith(LOGOUT_REQUEST_PATH);
  }

  private AuthenticationStatus login(String login, String password, HttpMessageContext context) {
    AuthenticationStatus authenticationStatus;

    CredentialValidationResult credentialValidationResult =
        identityStoreHandler.validate(new UsernamePasswordCredential(login, password));

    if (loggedCorrectly(credentialValidationResult.getStatus())) {
      authenticationStatus = createToken(credentialValidationResult, context);
    } else {
      authenticationStatus = context.responseUnauthorized();
    }

    return authenticationStatus;
  }


  private AuthenticationStatus login(String socialMediaAuthenticationToken,
      HttpMessageContext context) {
    AuthenticationStatus authenticationStatus;

    User user = null;
    try {
      user = facebookAPIConnector.getUser(socialMediaAuthenticationToken);
    } catch (Exception ignored) {
    }
    if (user == null) {
      try {
        user = googleAPIConnector.getUser(socialMediaAuthenticationToken);
      } catch (Exception ignored) {
      }
    }

    try {
      if (user != null) {
        user = userService.getOrCreateSocialMedia(user);
        authenticationStatus = createToken(user, context);
      } else {
        authenticationStatus = SEND_FAILURE;
      }
    } catch (Exception e) {
      authenticationStatus = SEND_FAILURE;
    }

    return authenticationStatus;
  }

  private AuthenticationStatus logout(String accessToken, String refreshToken,
      HttpMessageContext context) {
    addOldTokenToExpiredTokensList(accessToken);
    addOldTokenToExpiredTokensList(refreshToken);

    return context.doNothing();
  }

  private AuthenticationStatus validateRefreshToken(String token, HttpMessageContext context) {
    AuthenticationStatus authenticationStatus;

    try {
      validateTokenNotInExpiredTokensList(token);
      tokenProvider.validateToken(token, TokenType.REFRESH_TOKEN);

      JwtCredential jwtCredential = tokenProvider.getCredential(token, TokenType.REFRESH_TOKEN);

      addOldTokenToExpiredTokensList(token);

      authenticationStatus = createToken(jwtCredential, context);
    } catch (Exception e) {
      authenticationStatus = context.responseUnauthorized();
    }

    return authenticationStatus;
  }

  private void validateTokenNotInExpiredTokensList(String token) {
    if (expiredTokenService.isTokenInExpiredTokensList(token)) {
      throw new TokenInExpiredTokensListException();
    }
  }

  private void addOldTokenToExpiredTokensList(String token) {
    expiredTokenService.addTokenToExpiredTokensList(token);
  }

  private boolean loggedCorrectly(Status status) {
    return status == CredentialValidationResult.Status.VALID;
  }

  private AuthenticationStatus createToken(CredentialValidationResult result,
      HttpMessageContext context) {

    String accessToken = tokenProvider.createToken(result.getCallerPrincipal().getName(),
        result.getCallerGroups(), TokenType.ACCESS_TOKEN);

    String refreshToken = tokenProvider.createToken(result.getCallerPrincipal().getName(),
        result.getCallerGroups(), TokenType.REFRESH_TOKEN);

    var user = new CurrentUser();
    user.setEmail(result.getCallerPrincipal().getName());
    user.setRoles(result.getCallerGroups());
    user.setAccessToken(accessToken);
    user.setRefreshToken(refreshToken);
    authenticatedEvent.fire(user);

    return context.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
  }

  private AuthenticationStatus createToken(User user,
      HttpMessageContext context) {

    String principal = user.getEmail();

    Set<String> authorities = user.getRoles().stream()
        .map(UserRole::getRole)
        .collect(toSet());

    String accessToken = tokenProvider
        .createToken(user.getEmail(), authorities, TokenType.ACCESS_TOKEN);

    String refreshToken = tokenProvider
        .createToken(user.getEmail(), authorities, TokenType.REFRESH_TOKEN);

    var currentUserUser = new CurrentUser();
    currentUserUser.setEmail(user.getEmail());
    currentUserUser.setRoles(authorities);
    currentUserUser.setAccessToken(accessToken);
    currentUserUser.setRefreshToken(refreshToken);
    authenticatedEvent.fire(currentUserUser);

    return context.notifyContainerAboutLogin(principal, authorities);
  }

  private AuthenticationStatus createToken(JwtCredential jwtCredential,
      HttpMessageContext context) {

    String accessToken = tokenProvider.createToken(jwtCredential.getPrincipal(),
        jwtCredential.getAuthorities(), TokenType.ACCESS_TOKEN);

    String refreshToken = tokenProvider.createToken(jwtCredential.getPrincipal(),
        jwtCredential.getAuthorities(), TokenType.REFRESH_TOKEN);

    var user = new CurrentUser();
    user.setEmail(jwtCredential.getPrincipal());
    user.setRoles(jwtCredential.getAuthorities());
    user.setAccessToken(accessToken);
    user.setRefreshToken(refreshToken);
    authenticatedEvent.fire(user);

    return context.notifyContainerAboutLogin(jwtCredential.getPrincipal(),
        jwtCredential.getAuthorities());
  }

}
