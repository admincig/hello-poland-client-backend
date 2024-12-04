package pl.hellopoland.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import pl.hellopoland.bo.User;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.security.token.JwtCredential;
import pl.hellopoland.service.UserService;

import java.util.Optional;
import java.util.Set;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;

@RequestScoped
public class Authentication implements IdentityStore {

  @Inject
  private UserService userDao;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Override
  public CredentialValidationResult validate(Credential credential) {
    if (credential instanceof UsernamePasswordCredential) {
      UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;

      Optional<User> user = userDao.findUndeletedAndUnblockedByEmail(usernamePassword.getCaller());

      if (user.isPresent() && passwordEncoder.matches(new String(usernamePassword.getPassword().getValue()), user.get().getPassword())) {
        return new CredentialValidationResult(usernamePassword.getCaller());
      }
    }
    if (credential instanceof JwtCredential
        && userDao.findUndeletedAndUnblockedByEmail(((JwtCredential) credential).getPrincipal()).isPresent()) {
      return new CredentialValidationResult(((JwtCredential) credential).getPrincipal());
    }

    return NOT_VALIDATED_RESULT;
  }

  @Override
  public Set<ValidationType> validationTypes() {
    return Set.of(ValidationType.VALIDATE);
  }
}
