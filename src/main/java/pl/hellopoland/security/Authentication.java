package pl.hellopoland.security;

import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import pl.hellopoland.bo.User;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.security.token.JwtCredential;
import pl.hellopoland.service.UserService;

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

      Optional<User> user = userDao.findUndeletedByEmail(usernamePassword.getCaller());

      if (user.isPresent()
          && (user.get().getPartner() == null || !user.get().getPartner().isBlocked())
          && passwordEncoder.matches(
              new String(usernamePassword.getPassword().getValue()), user.get().getPassword())) {
        return new CredentialValidationResult(usernamePassword.getCaller());
      }
    }
    if (credential instanceof JwtCredential
        && userDao.findUndeletedByEmail(((JwtCredential) credential).getPrincipal()).isPresent()) {
      return new CredentialValidationResult(((JwtCredential) credential).getPrincipal());
    }

    return NOT_VALIDATED_RESULT;
  }

  @Override
  public Set<ValidationType> validationTypes() {
    return Set.of(ValidationType.VALIDATE);
  }
}
