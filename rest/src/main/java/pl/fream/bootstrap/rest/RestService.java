package pl.fream.bootstrap.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class RestService {
	
	@GET
	public String entryPoint() {
		return "REST Service is running";
	}
}
