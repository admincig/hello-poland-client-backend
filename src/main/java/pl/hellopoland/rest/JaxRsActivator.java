package pl.hellopoland.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/v1")
@DeclareRoles({"root", "admin", "helpdesk_partner_manager", "helpdesk_content_manager",
    "helpdesk_support", "partner", "partner_admin", "partner_salesman", "salesman", "usher",
    "user"})
@OpenAPIDefinition(servers = @Server(url = "/api"))
public class JaxRsActivator extends Application {

}
