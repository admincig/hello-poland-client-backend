package pl.hellopoland.partner;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@DeclareRoles({"root", "admin", "user"})
public class JaxRsActivator extends Application {

}
