package pl.hellopoland.partner;

import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import pl.hellopoland.partner.password.PasswordEncoder;
import pl.hellopoland.user.UserRole;
import pl.hellopoland.user.UserService;

@RequestScoped
public class JpaIdentityStore implements IdentityStore {

  @Inject
  private UserService userDao;

  @Override
  public CredentialValidationResult validate(Credential credential) {

    if (credential instanceof UsernamePasswordCredential) {
      UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;

      var user = userDao.findByEmail(usernamePassword.getCaller());
      if (new PasswordEncoder().matches(new String(usernamePassword.getPassword().getValue()),
          user.getPassword())) {
        return new CredentialValidationResult(usernamePassword.getCaller(),
            user.getRoles().stream().map(UserRole::getRole).collect(Collectors.toSet()));
      }
    }
    return NOT_VALIDATED_RESULT;
  }

  @Override
  public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
    return validationResult.getCallerGroups();
  }

}
