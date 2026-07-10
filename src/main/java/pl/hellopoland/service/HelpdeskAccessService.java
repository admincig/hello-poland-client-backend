package pl.hellopoland.service;

import pl.hellopoland.bo.Partner;
import pl.hellopoland.bo.Sight;
import pl.hellopoland.bo.SightEvent;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserRole.Role;
import pl.hellopoland.config.PartnerPagedCollectionConfig;
import pl.hellopoland.config.SightEventPagedCollectionConfig;
import pl.hellopoland.config.SightPagedCollectionConfig;
import pl.hellopoland.exception.notfound.AccessDeniedException;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@LocalBean
@Stateless
public class HelpdeskAccessService extends ServiceSuperclass {

  public void applyPartnerScope(PartnerPagedCollectionConfig config) {
    User user = getLoggedUser();
    if (hasGlobalHelpdeskAccess(user)) {
      return;
    }

    Set<Long> partnerIds = allowedPartnerIds(user);
    Set<Long> sightPartnerIds = allowedSights(user).stream()
        .map(Sight::getPartner)
        .map(Partner::getId)
        .collect(Collectors.toSet());

    if (partnerIds.isEmpty() && sightPartnerIds.isEmpty()) {
      return;
    }
    if (partnerIds.isEmpty()) {
      config.setIds(sightPartnerIds);
      return;
    }
    if (sightPartnerIds.isEmpty()) {
      config.setIds(partnerIds);
      return;
    }

    partnerIds.retainAll(sightPartnerIds);
    config.setIds(partnerIds);
  }

  public void applySightScope(SightPagedCollectionConfig config) {
    User user = getLoggedUser();
    if (hasGlobalHelpdeskAccess(user)) {
      return;
    }

    Set<Long> partnerIds = allowedPartnerIds(user);
    Set<Long> sightIds = allowedSightIds(user);
    if (!partnerIds.isEmpty()) {
      config.setPartnerIds(partnerIds);
    }
    if (!sightIds.isEmpty()) {
      config.setIds(sightIds);
    }
  }

  public void applySightEventScope(SightEventPagedCollectionConfig config) {
    User user = getLoggedUser();
    if (hasGlobalHelpdeskAccess(user)) {
      return;
    }

    Set<Long> partnerIds = allowedPartnerIds(user);
    Set<Long> sightIds = allowedSightIds(user);
    if (!partnerIds.isEmpty()) {
      config.setPartnerIds(partnerIds);
    }
    if (!sightIds.isEmpty()) {
      config.setSightIds(sightIds);
    }
  }

  public void requirePartnerAccess(Partner partner) {
    User user = getLoggedUser();
    if (hasGlobalHelpdeskAccess(user)) {
      return;
    }
    if (partner == null) {
      throw new AccessDeniedException();
    }

    Set<Long> partnerIds = allowedPartnerIds(user);
    Set<Long> sightPartnerIds = allowedSights(user).stream()
        .map(Sight::getPartner)
        .map(Partner::getId)
        .collect(Collectors.toSet());
    if (partnerIds.isEmpty() && sightPartnerIds.isEmpty()) {
      return;
    }
    boolean partnerAllowed = partnerIds.isEmpty() || partnerIds.contains(partner.getId());
    boolean sightPartnerAllowed = sightPartnerIds.isEmpty() || sightPartnerIds.contains(partner.getId());
    if (!partnerAllowed || !sightPartnerAllowed) {
      throw new AccessDeniedException();
    }
  }

  public void requireSightAccess(Sight sight) {
    User user = getLoggedUser();
    if (hasGlobalHelpdeskAccess(user) || sight == null) {
      return;
    }

    Set<Long> partnerIds = allowedPartnerIds(user);
    Set<Long> sightIds = allowedSightIds(user);
    boolean partnerAllowed = partnerIds.isEmpty()
        || (sight.getPartner() != null && partnerIds.contains(sight.getPartner().getId()));
    boolean sightAllowed = sightIds.isEmpty() || sightIds.contains(sight.getId());
    if (!partnerAllowed || !sightAllowed) {
      throw new AccessDeniedException();
    }
  }

  public void requireSightEventAccess(SightEvent sightEvent) {
    requireSightAccess(sightEvent != null ? sightEvent.getSight() : null);
  }

  private boolean hasGlobalHelpdeskAccess(User user) {
    return user == null || user.hasRole(Role.ROOT) || user.hasRole(Role.ADMIN)
        || user.hasRole(Role.SALESMAN) || user.getPartner() != null;
  }

  private Set<Partner> allowedPartners(User user) {
    return user != null && user.getAllowedHelpdeskPartners() != null
        ? new HashSet<>(user.getAllowedHelpdeskPartners())
        : new HashSet<>();
  }

  private Set<Sight> allowedSights(User user) {
    return user != null && user.getAllowedHelpdeskSights() != null
        ? new HashSet<>(user.getAllowedHelpdeskSights())
        : new HashSet<>();
  }

  private Set<Long> allowedPartnerIds(User user) {
    return allowedPartners(user).stream().map(Partner::getId).collect(Collectors.toSet());
  }

  private Set<Long> allowedSightIds(User user) {
    return allowedSights(user).stream().map(Sight::getId).collect(Collectors.toSet());
  }
}
