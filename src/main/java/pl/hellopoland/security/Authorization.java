package pl.hellopoland.security;

import pl.hellopoland.service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import java.util.Set;

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
