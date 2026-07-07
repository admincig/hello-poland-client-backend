package pl.hellopoland.security;

import pl.hellopoland.bo.User;
import pl.hellopoland.security.password.PasswordEncoder;
import pl.hellopoland.security.token.JwtCredential;
import pl.hellopoland.service.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

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

            UsernamePasswordCredential usernamePassword =
                    (UsernamePasswordCredential) credential;

            Optional<User> user =
                    userDao.findUndeletedByEmail(usernamePassword.getCaller());

            if (user.isPresent()) {

                User u = user.get();

                boolean isMarketUser =
                        u.hasRole(pl.hellopoland.bo.UserRole.Role.USER)
                                && u.getPartner() == null;

                if ((!isMarketUser || u.isEmailVerified())
                        && !u.isBlocked()
                        && (u.getPartner() == null || !u.getPartner().isBlocked())
                        && passwordEncoder.matches(
                        new String(usernamePassword.getPassword().getValue()),
                        u.getPassword())) {

                    Set<String> groups = u.getRoles().stream()
                            .map(ur -> ur.getRole().toString())
                            .collect(toSet());

                    return new CredentialValidationResult(
                            usernamePassword.getCaller(),
                            groups
                    );
                }
            }
        }

        if (credential instanceof JwtCredential
                && userDao.findUndeletedByEmail(
                ((JwtCredential) credential).getPrincipal()
        ).filter(user -> !user.isBlocked()).isPresent()) {

            return new CredentialValidationResult(
                    ((JwtCredential) credential).getPrincipal()
            );
        }

        return NOT_VALIDATED_RESULT;
    }

  @Override
  public Set<ValidationType> validationTypes() {
    return Set.of(ValidationType.VALIDATE);
  }
}
