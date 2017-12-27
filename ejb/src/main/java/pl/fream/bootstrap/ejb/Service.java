package pl.fream.bootstrap.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@LocalBean
@Stateless
public class Service {
	
	@Inject
	private Logger log;
	@Inject
	private EntityManager em;
	
	public String greetings() {
		log.info("Greetings");
		List<User> users = em.createQuery("from User", User.class).getResultList();
		log.info("Found " + users.size() + " users");
		return "EJB Service is working";
	}
}
