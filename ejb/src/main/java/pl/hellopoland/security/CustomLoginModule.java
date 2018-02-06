package pl.hellopoland.security;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class CustomLoginModule extends AbstractServerLoginModule {

  Logger logger = Logger.getLogger(CustomLoginModule.class.getName());

  Principal identity;
  DataSource ds;

  @Override
  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String, ?> sharedState, Map<String, ?> options) {
    super.initialize(subject, callbackHandler, sharedState, options);
    try {
      ds = (DataSource) new InitialContext().lookup("java:jboss/datasources/hellopoland");
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
      SecureStore ss = getUsernameAndPassword();
      loginOk = findEmailInDatabase(ss.name, ss.password);
      if (loginOk) {
        identity = super.createIdentity(ss.name);
      }
      return loginOk;
    } catch (Exception e) {
      LoginException le = new LoginException("Failed to get user: " + e.getMessage());
      le.initCause(e);
      throw le;
    }
  }

  private boolean findEmailInDatabase(String email, char[] password) throws SQLException {
    Connection conn = ds.getConnection();
    PreparedStatement ps = conn.prepareStatement("select count(*) from Users where email=?");
    ps.setString(1, email);
    ps.execute();
    ResultSet rs = ps.getResultSet();
    rs.next();
    long c = rs.getLong(1);
    conn.close();
    return c > 0;
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
    return new Group[] {g};
  }

  protected SecureStore getUsernameAndPassword() throws LoginException {
    SecureStore info = new SecureStore();
    NameCallback nc = new NameCallback("User name: ", "guest");
    PasswordCallback pc = new PasswordCallback("Password: ", false);
    Callback[] callbacks = {nc, pc};

    try {
      callbackHandler.handle(callbacks);
      info.name = nc.getName();
      info.password = pc.getPassword();
      pc.clearPassword();

    } catch (IOException e) {
      LoginException le = new LoginException("Failed to get username/password");
      le.initCause(e);
      throw le;
    } catch (UnsupportedCallbackException e) {
      LoginException le =
          new LoginException("CallbackHandler does not support: " + e.getCallback());
      le.initCause(e);
      throw le;
    }
    return info;
  }

  private class SecureStore {
    String name;
    char[] password;
  }

}
