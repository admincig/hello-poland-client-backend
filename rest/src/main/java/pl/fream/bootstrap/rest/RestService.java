package pl.fream.bootstrap.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import pl.fream.bootstrap.ejb.Service;

@Path("/")
@RequestScoped
public class RestService {
	
	@Inject
	Service service;
	
	@GET
	public String entryPoint() {
		return service.greetings() + " and REST Service is running!";
	}
}
