package pl.hellopoland.security;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.AbstractServerLoginModule;
import pl.hellopoland.user.User;
import pl.hellopoland.user.UserService;
import pl.hellopoland.util.FacebookAPIConnector;

public class FacebookLoginModule extends AbstractServerLoginModule {

  Logger logger = Logger.getLogger(FacebookLoginModule.class.getName());

  Principal identity;
  DataSource ds;
  UserService userService;
  FacebookAPIConnector fbConnector;


  @Override
  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String, ?> sharedState, Map<String, ?> options) {
    super.initialize(subject, callbackHandler, sharedState, options);
    try {
      InitialContext ctx = new InitialContext();
      ds = (DataSource) ctx.lookup("java:jboss/datasources/hellopoland");
      userService = (UserService) ctx.lookup("java:app/hellopoland.ejb/UserService");
      fbConnector = new FacebookAPIConnector();
    } catch (NamingException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected Principal getIdentity() {
    return identity;
  }

  @Override
  public boolean login() throws LoginException {
    try {
      User user = fbConnector.getUser(getToken());
      loginOk = user != null;
      if (loginOk) {
        user = userService.getOrCreateSocialMedia(user);
        identity = super.createIdentity(user.getEmail());
      }
      return loginOk;
    } catch (Exception e) {
      logger.warning(e.getMessage());
      LoginException le = new LoginException("Failed to get user: " + e.getMessage());
      le.initCause(e);
      throw le;
    }
  }

  @Override
  protected Group[] getRoleSets() throws LoginException {
    try {
      return findRolesInDatabase();
    } catch (Exception e) {
      LoginException le = new LoginException("Failed to get roles: " + e.getMessage());
      le.initCause(e);
      throw le;
    }
  }

  @SuppressWarnings("deprecation")
  private Group[] findRolesInDatabase() throws Exception {
    Connection conn = ds.getConnection();
    PreparedStatement ps = conn.prepareStatement(
        "select ur.role from userrole ur join users u on u.id = ur.user_id where u.email=?");
    ps.setString(1, getIdentity().getName());
    ps.execute();
    ResultSet rs = ps.getResultSet();
    Group g = new SimpleGroup("Roles");
    while (rs.next()) {
      String role = rs.getString(1);
      g.addMember(createIdentity(role));
    }
    conn.close();
    return new Group[] {g};
  }

  protected String getToken() throws LoginException {
    NameCallback nc = new NameCallback("User name: ", "guest");
    PasswordCallback pc = new PasswordCallback("Password: ", false);
    Callback[] callbacks = {nc, pc};

    try {
      callbackHandler.handle(callbacks);
      return new String(pc.getPassword());
    } catch (IOException e) {
      LoginException le = new LoginException("Failed to get username/password");
      le.initCause(e);
      throw le;
    } catch (UnsupportedCallbackException e) {
      LoginException le =
          new LoginException("CallbackHandler does not support: " + e.getCallback());
      le.initCause(e);
      throw le;
    } finally {
      pc.clearPassword();
    }
  }

}
