package pl.fream.bootstrap.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class Service {
	
	public String greetings() {
		return "EJB Service is working";
	}
}
