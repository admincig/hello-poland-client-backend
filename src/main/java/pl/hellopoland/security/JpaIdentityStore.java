package pl.hellopoland.security;

import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole;
import pl.hellopoland.service.UserService;

@RequestScoped
public class JpaIdentityStore implements IdentityStore {

  @Inject
  private UserService userDao;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Override
  public CredentialValidationResult validate(Credential credential) {
    if (credential instanceof UsernamePasswordCredential) {
      UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;

      Optional<User> user = userDao.findByEmail(usernamePassword.getCaller());

      if (user.isPresent() && passwordEncoder.matches(
          new String(usernamePassword.getPassword().getValue()), user.get().getPassword())) {
        return new CredentialValidationResult(usernamePassword.getCaller(),
            user.get().getRoles().stream().map(UserRole::getRole).collect(Collectors.toSet()));
      }
    }

    return NOT_VALIDATED_RESULT;
  }

  @Override
  public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
    return validationResult.getCallerGroups();
  }

}
