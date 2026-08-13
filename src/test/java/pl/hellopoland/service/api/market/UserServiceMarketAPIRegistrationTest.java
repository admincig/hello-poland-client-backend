package pl.hellopoland.service.api.market;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.dto.UserInfoDTO;
import pl.hellopoland.exception.conflict.ConflictingException;
import pl.hellopoland.service.UserService;

public class UserServiceMarketAPIRegistrationTest {

  private final UserServiceMarketAPI api = new UserServiceMarketAPI();
  private final RecordingUserService userService = new RecordingUserService();
  private final RecordingMailingListService mailingListService =
      new RecordingMailingListService();

  @Before
  public void setUp() {
    api.service = userService;
    api.mailingListAPI = mailingListService;
  }

  @Test
  public void delegatesRegistrationWithNormalizedEmail() {
    UserInfoDTO dto = validRegistration();
    dto.email = "  Test.User@Example.com ";

    api.register(dto);

    assertTrue(userService.registrationCalled);
    assertEquals("test.user@example.com", userService.email);
    assertEquals("secret", userService.password);
    assertTrue(userService.tosAgreement);
  }

  @Test
  public void addsSuccessfullyRegisteredUserToMailingListWhenRequested() {
    UserInfoDTO dto = validRegistration();
    dto.addToMailingList = true;

    api.register(dto);

    assertEquals("test@example.com", mailingListService.email);
  }

  @Test(expected = ConflictingException.class)
  public void rejectsRegistrationWithoutEmailBeforeCallingService() {
    UserInfoDTO dto = validRegistration();
    dto.email = null;

    api.register(dto);
  }

  @Test(expected = ConflictingException.class)
  public void rejectsRegistrationWithBlankEmailBeforeCallingService() {
    UserInfoDTO dto = validRegistration();
    dto.email = "   ";

    api.register(dto);
  }

  private UserInfoDTO validRegistration() {
    UserInfoDTO dto = new UserInfoDTO();
    dto.email = "test@example.com";
    dto.password = "secret";
    dto.passwordConfirmation = "secret";
    dto.tosAgreement = true;
    return dto;
  }

  private static class RecordingUserService extends UserService {
    private boolean registrationCalled;
    private String email;
    private String password;
    private boolean tosAgreement;

    @Override
    public User registerMarketUser(String email, String password, boolean tosAgreement) {
      this.registrationCalled = true;
      this.email = email;
      this.password = password;
      this.tosAgreement = tosAgreement;

      User user = new User(Role.USER);
      user.setEmail(email);
      return user;
    }
  }

  private static class RecordingMailingListService extends MailingListServiceMarketAPI {
    private String email;

    @Override
    public String addToMailingList(String email) {
      this.email = email;
      return email;
    }
  }
}
