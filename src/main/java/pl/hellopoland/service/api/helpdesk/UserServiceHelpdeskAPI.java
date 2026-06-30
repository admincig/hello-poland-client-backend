package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.User;
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

    @RolesAllowed("admin")
    public UserORO me() {
        User bo = service.me();
        return new UserORO(bo);
    }

    @RolesAllowed("admin")
    public void updatePassword(Long id, String newPassword) {
        User bo = service.get(id);
        service.updatePasswordForUser(bo, newPassword);
    }

    @RolesAllowed("admin")
    public void deleteUser(Long id) {
        User bo = service.get(id);
        service.deletePartnerUserFromHelpdesk(bo);
    }

    @RolesAllowed("admin")
    public List<UserDTO> getPartnerUsers(Long partnerId) {
        return service.getPartnerUsersForHelpdesk(partnerId);
    }

    @RolesAllowed("admin")
    public UserDTO createPartnerUser(Long partnerId, UserDTO dto) {
        return service.createPartnerPanelUserFromHelpdesk(partnerId, dto);
    }

    @RolesAllowed("admin")
    public UserDTO updatePartnerUser(Long partnerId, Long userId, UserDTO dto) {
        return service.updatePartnerPanelUserFromHelpdesk(partnerId, userId, dto);
    }

    @RolesAllowed("admin")
    public void changePartnerUserPassword(Long partnerId, Long userId, String password) {
        service.changePasswordForPartnerUserFromHelpdesk(partnerId, userId, password);
    }

    @RolesAllowed("admin")
    public void deletePartnerUser(Long partnerId, Long userId) {
        service.deletePartnerPanelUserFromHelpdesk(partnerId, userId);
    }
}
