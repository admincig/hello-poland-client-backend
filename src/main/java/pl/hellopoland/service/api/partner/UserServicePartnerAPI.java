package pl.hellopoland.service.api.partner;

import pl.hellopoland.bo.User;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.rest.dto.PagedCollection;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.PartnerService;
import pl.hellopoland.service.TranslationService;
import pl.hellopoland.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class UserServicePartnerAPI {

  @Inject
  UserService service;
  @Inject
  PartnerService partnerService;
  @Inject
  TranslationService transService;

  @RolesAllowed("partner")
  public UserORO me() {
    User bo = service.me();
    return new UserORO(bo);
  }

  @RolesAllowed("partner")
  public void changePasswordForLoggedPartner(UserAuthDTO userDTO) {
    service.changePasswordForLoggedPartner(userDTO);
  }

  @RolesAllowed("partner")
  public PagedCollection<UserDTO> getPartnerPanelUsers() {
    return new PagedCollection<>(service.getPartnerPanelUsersForLoggedPartner(), null);
  }

  @RolesAllowed("partner")
  public UserDTO getPartnerPanelUser(long userId) {
    return service.getPartnerPanelUserForLoggedPartner(userId);
  }

  @RolesAllowed("partner")
  public UserDTO createPartnerPanelUser(UserDTO userDTO) {
    return service.createPartnerPanelUserForLoggedPartner(userDTO);
  }

  @RolesAllowed("partner")
  public UserDTO updatePartnerPanelUser(long userId, UserDTO userDTO) {
    return service.updatePartnerPanelUserForLoggedPartner(userId, userDTO);
  }

  @RolesAllowed("partner")
  public void changePasswordForPartnerPanelUser(long userId, UserAuthDTO userDTO) {
    service.changePasswordForPartnerPanelUser(userId, userDTO);
  }

  @RolesAllowed("partner")
  public void deletePartnerPanelUser(long userId) {
    service.deletePartnerPanelUserForLoggedPartner(userId);
  }

  @RolesAllowed("partner")
  public UserDTO setPartnerPanelUserBlocked(long userId, boolean blocked) {
    return service.setPartnerPanelUserBlockedForLoggedPartner(userId, blocked);
  }

  @RolesAllowed("partner")
  public void changePasswordForUsher(long usherId, UserAuthDTO usherDTO) {
    service.changePasswordForUsher(usherId, usherDTO);
  }

  @RolesAllowed("partner")
  public UserDTO createUsher(UserDTO usherDTO) {
    return service.createUsherForLoggedPartner(usherDTO);
  }

  @RolesAllowed("partner")
  public PagedCollection<UserDTO> getUshers() {
    return new PagedCollection<>(service.getUshers(), null);
  }

  @RolesAllowed("partner")
  public UserDTO getUsher(long usherId) {
    return service.getUsher(usherId);
  }

  @RolesAllowed("partner")
  public UserDTO updateUsher(UserDTO usher) {
    return service.updateUsher(usher);
  }

  @RolesAllowed("partner")
  public UserDTO setUsherBlocked(long usherId, boolean blocked) {
    return service.setUsherBlockedForLoggedPartner(usherId, blocked);
  }

  @RolesAllowed("partner")
  public void deleteUsherForCurrentPartner(long usherId) {
        service.deleteUsher(usherId);
  }
}
