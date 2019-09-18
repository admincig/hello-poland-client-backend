package pl.hellopoland.security;

import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import pl.hellopoland.service.UserService;

@RequestScoped
public class Authorization implements IdentityStore {

  @Inject
  UserService userDao;

  @Override
  public Set<ValidationType> validationTypes() {
    return Set.of(ValidationType.PROVIDE_GROUPS);
  }

  @Override
  public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
    return userDao.getFlatRoles(validationResult.getCallerPrincipal().getName());
  }
}
