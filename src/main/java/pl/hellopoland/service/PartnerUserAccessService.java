package pl.hellopoland.service;

import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.exception.notfound.AccessDeniedException;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@LocalBean
@Stateless
public class PartnerUserAccessService extends ServiceSuperclass {

  public boolean isPartnerAdmin(User user) {
    return user != null && user.hasRole(Role.PARTNER_ADMIN);
  }

  public boolean isPartnerSalesman(User user) {
    return user != null && user.hasRole(Role.PARTNER_SALESMAN);
  }

  public boolean hasFullPartnerAccess(User user) {
    return user != null && user.hasRole(Role.PARTNER) && !isPartnerSalesman(user);
  }

  public void requireFullPartnerAccess() {
    if (!hasFullPartnerAccess(getLoggedUser())) {
      throw new AccessDeniedException();
    }
  }

  public void requireCanManagePartnerUsers() {
    requireFullPartnerAccess();
  }

  public void requireCanManageSights() {
    requireFullPartnerAccess();
  }

  public List<Sight> filterAllowedSights(Collection<Sight> sights) {
    User user = getLoggedUser();
    if (!isPartnerSalesman(user)) {
      return List.copyOf(sights);
    }

    Set<Long> allowedIds = getAllowedSightIds(user);
    return sights.stream()
        .filter(sight -> sight != null && allowedIds.contains(sight.getId()))
        .collect(Collectors.toList());
  }

  public void requireCanAccessSight(Sight sight) {
    if (!canAccessSight(sight)) {
      throw new AccessDeniedException();
    }
  }

  public void requireCanAccessSightEvent(SightEvent sightEvent) {
    if (sightEvent == null) {
      throw new AccessDeniedException();
    }
    requireCanAccessSight(sightEvent.getSight());
  }

  public boolean canAccessSight(Sight sight) {
    User user = getLoggedUser();
    if (hasFullPartnerAccess(user)) {
      return true;
    }
    if (!isPartnerSalesman(user) || sight == null) {
      return false;
    }
    return getAllowedSightIds(user).contains(sight.getId());
  }

  public Set<Long> getAllowedSightIdsForLoggedUser() {
    return getAllowedSightIds(getLoggedUser());
  }

  private Set<Long> getAllowedSightIds(User user) {
    if (user == null || user.getAllowedPartnerSights() == null) {
      return Collections.emptySet();
    }
    return user.getAllowedPartnerSights().stream()
        .filter(Objects::nonNull)
        .map(Sight::getId)
        .collect(Collectors.toSet());
  }
}
