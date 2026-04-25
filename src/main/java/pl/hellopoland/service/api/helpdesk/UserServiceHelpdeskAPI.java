package pl.hellopoland.service.api.helpdesk;

import pl.hellopoland.bo.User;
import pl.hellopoland.rest.dto.UserORO;
import pl.hellopoland.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

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
}