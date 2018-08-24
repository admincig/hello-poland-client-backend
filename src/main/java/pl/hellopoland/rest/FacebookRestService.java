package pl.hellopoland.rest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/facebook")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacebookRestService {

  @GET
  // @Path("/#access_token")
  public String receiveUserAccessToken(@QueryParam(value = "#access_token") String accessToken) {
    return accessToken;
  }


  public static void main(String[] args) {
    try {

      String myAppId = "255694758406990";
      String myAppSecret = "1c338c26d8738665a893601b462a1f33";
      String redirectURI = "http://localhost:8080/hellopoland/v1/facebook";
      // user access token:
      String uri = "https://www.facebook.com/dialog/oauth?client_id=" + myAppId + "&redirect_uri="
          + redirectURI + "&response_type=token";
      // app access token:
      // String uri = "https://graph.facebook.com/oauth/access_token?client_id=" + myAppId
      // + "&client_secret=" + myAppSecret + "&grant_type=client_credentials" + "&redirect_uri="
      // + redirectURI + "&scope=user_about_me";

      URL newURL = new URL(uri);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      InputStream is = newURL.openStream();
      int r;
      while ((r = is.read()) != -1) {
        baos.write(r);
      }
      String response = new String(baos.toByteArray());
      is.close();
      baos.close();

      String TOKEN_INDEX = "accesss_token=";
      String token = response.substring(TOKEN_INDEX.length() - 1);// blad!!!

      // API Call using the application access token
      String graph = "https://graph.facebook.com/v3.1/" + myAppId + "?access_token=" + token;

      URL graphURL = new URL(graph);
      HttpURLConnection myWebClient = (HttpURLConnection) graphURL.openConnection();

      String responseMessage = myWebClient.getResponseMessage();

      if (myWebClient.getResponseCode() != HttpURLConnection.HTTP_OK) {
        System.out.println(myWebClient.getResponseCode() + " " + responseMessage);
      } else {
        System.out.println(responseMessage);
      }
      myWebClient.disconnect();

    } catch (Exception e) {
      System.err.println(e);
    }
  }
}
