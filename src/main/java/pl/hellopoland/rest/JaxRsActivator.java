package pl.hellopoland.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
@DeclareRoles({"root", "admin", "user"})
@OpenAPIDefinition(servers = @Server(url = "/api"))
public class JaxRsActivator extends Application {

}
