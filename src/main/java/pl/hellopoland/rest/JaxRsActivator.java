package pl.hellopoland.rest;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
@DeclareRoles({"root", "admin", "user"})
public class JaxRsActivator extends Application {

}
