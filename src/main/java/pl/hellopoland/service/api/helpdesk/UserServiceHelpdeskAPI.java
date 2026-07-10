package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.User;
import pl.hellopoland.dto.UserAuthDTO;
import pl.hellopoland.dto.UserDTO;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class UserServiceHelpdeskAPI {

    @Inject
    UserService service;

    @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
        "helpdesk_content_manager", "helpdesk_support"})
    public UserORO me() {
        User bo = service.me();
        return new UserORO(bo);
    }

    @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
        "helpdesk_content_manager", "helpdesk_support"})
    public void changeOwnPassword(UserAuthDTO dto) {
        service.changePasswordForLoggedUser(dto);
    }

    @RolesAllowed({"root", "admin", "salesman", "helpdesk_partner_manager",
        "helpdesk_content_manager", "helpdesk_support"})
    public UserORO updateOwnAvatar(byte[] bytes, String extension) {
        User bo = service.updateAvatarForLoggedUser(bytes, extension);
        return new UserORO(bo);
    }

    @RolesAllowed({"root", "admin"})
    public List<UserDTO> getHelpdeskUsers() {
        return service.getHelpdeskUsers();
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO getHelpdeskUser(Long id) {
        return service.getHelpdeskUser(id);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO createHelpdeskUser(UserDTO dto) {
        return service.createHelpdeskUser(dto);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO updateHelpdeskUser(Long id, UserDTO dto) {
        return service.updateHelpdeskUser(id, dto);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO setHelpdeskUserBlocked(Long id, boolean blocked) {
        return service.setHelpdeskUserBlocked(id, blocked);
    }

    @RolesAllowed({"root", "admin"})
    public void updatePassword(Long id, String newPassword) {
        service.changeHelpdeskUserPassword(id, newPassword);
    }

    @RolesAllowed({"root", "admin"})
    public void deleteUser(Long id) {
        service.deleteHelpdeskUser(id);
    }

    @RolesAllowed({"root", "admin"})
    public List<UserDTO> getPartnerUsers(Long partnerId) {
        return service.getPartnerUsersForHelpdesk(partnerId);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO createPartnerUser(Long partnerId, UserDTO dto) {
        return service.createPartnerPanelUserFromHelpdesk(partnerId, dto);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO updatePartnerUser(Long partnerId, Long userId, UserDTO dto) {
        return service.updatePartnerPanelUserFromHelpdesk(partnerId, userId, dto);
    }

    @RolesAllowed({"root", "admin"})
    public void changePartnerUserPassword(Long partnerId, Long userId, String password) {
        service.changePasswordForPartnerUserFromHelpdesk(partnerId, userId, password);
    }

    @RolesAllowed({"root", "admin"})
    public void deletePartnerUser(Long partnerId, Long userId) {
        service.deletePartnerPanelUserFromHelpdesk(partnerId, userId);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO setPartnerUserBlocked(Long partnerId, Long userId, boolean blocked) {
        return service.setPartnerPanelUserBlockedFromHelpdesk(partnerId, userId, blocked);
    }

    @RolesAllowed({"root", "admin"})
    public List<UserDTO> getPartnerUshers(Long partnerId) {
        return service.getUshersForPartnerFromHelpdesk(partnerId);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO createPartnerUsher(Long partnerId, UserDTO dto) {
        return service.createUsherForPartnerFromHelpdesk(partnerId, dto);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO updatePartnerUsher(Long partnerId, Long usherId, UserDTO dto) {
        return service.updateUsherForPartnerFromHelpdesk(partnerId, usherId, dto);
    }

    @RolesAllowed({"root", "admin"})
    public void changePartnerUsherPassword(Long partnerId, Long usherId, String password) {
        UserAuthDTO dto = new UserAuthDTO();
        dto.password = password;
        service.changeUsherPasswordForPartnerFromHelpdesk(partnerId, usherId, dto);
    }

    @RolesAllowed({"root", "admin"})
    public void deletePartnerUsher(Long partnerId, Long usherId) {
        service.deleteUsherForPartnerFromHelpdesk(partnerId, usherId);
    }

    @RolesAllowed({"root", "admin"})
    public UserDTO setPartnerUsherBlocked(Long partnerId, Long usherId, boolean blocked) {
        return service.setUsherBlockedForPartnerFromHelpdesk(partnerId, usherId, blocked);
    }
}
