package pl.hellopoland.partner;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@DeclareRoles({"root", "admin", "user"})
@ApplicationPath("/")
public class JaxRsActivator extends Application {

}
