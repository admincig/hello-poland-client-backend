package pl.hellopoland.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import pl.hellopoland.service.Service;

@Path("/")
@RequestScoped
public class RestService {

  @Inject
  Service service;

  @Context
  HttpServletRequest req;

  @GET
  public String greetings() {
    return service.greetings() + " and REST Service is running!";
  }

  @POST
  public String login(String email) throws ServletException {
    req.login(email, "");
    service.secured();
    return "security is working";
  }
}
