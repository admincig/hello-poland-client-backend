package pl.hellopoland.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole.Role;

public class UserServiceMarketPasswordResetTest {

  private final UserService service = new UserService();

  @Test
  public void acceptsActiveVerifiedPortalUser() {
    User user = userWithRole(Role.USER);

    assertTrue(service.isActiveMarketUser(user));
  }

  @Test
  public void rejectsHelpdeskUser() {
    User user = userWithRole(Role.HELPDESK_PARTNER_MANAGER);

    assertFalse(service.isActiveMarketUser(user));
  }

  @Test
  public void rejectsUnverifiedPortalUser() {
    User user = userWithRole(Role.USER);
    user.setEmailVerified(false);

    assertFalse(service.isActiveMarketUser(user));
  }

  @Test
  public void rejectsBlockedPortalUser() {
    User user = userWithRole(Role.USER);
    user.setBlocked(true);

    assertFalse(service.isActiveMarketUser(user));
  }

  @Test
  public void rejectsDeletedPortalUser() {
    User user = userWithRole(Role.USER);
    user.setDeleted(true);

    assertFalse(service.isActiveMarketUser(user));
  }

  private User userWithRole(Role role) {
    User user = new User(role);
    user.setEmail("user@example.com");
    user.setEmailVerified(true);
    return user;
  }
}
