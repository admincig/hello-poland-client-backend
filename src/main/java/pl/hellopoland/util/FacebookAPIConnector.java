package pl.hellopoland.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import pl.hellopoland.bo.User;
import pl.hellopoland.bo.UserLocation;

public class FacebookAPIConnector {

  private static final String FACEBOOK_API_HOST =
      "https://graph.facebook.com/me?fields=email,name,picture.type(large),location&access_token=";

  private Logger logger = Logger.getLogger(FacebookAPIConnector.class.getName());

  public User getUser(String token) {
    try {
      logger.info(FACEBOOK_API_HOST + token);
      JsonObject me = me(token);
      logger.info(me.toString());
      User user = new User();
      user.setEmail(me.getString("email"));
      user.setName(me.getString("name"));
      user.setPicture(me.getJsonObject("picture").getJsonObject("data").getString("url"));
      JsonObject jsonLocation = me.getJsonObject("location");
      if (jsonLocation != null) {
        UserLocation location = new UserLocation();
        location.setCity(NameAndAddressSplitter.getCity(jsonLocation.getString("name")));
        location.setCountry(NameAndAddressSplitter.getCountry(jsonLocation.getString("name")));
        user.setLocation(location);

      }
      return user;
    } catch (Exception e) {
      return null;
    }
  }

  private JsonObject me(String token) throws IOException {
    URL url = new URL(FACEBOOK_API_HOST + token);
    URLConnection conn = url.openConnection();
    InputStream is = conn.getInputStream();
    return Json.createReader(is).readObject();
  }



  public static void main(String... strings) {
    FacebookFanPagePostReader.getInstance().readPosts();
  }
}
