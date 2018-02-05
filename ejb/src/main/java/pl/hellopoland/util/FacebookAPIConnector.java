package pl.hellopoland.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.json.Json;
import javax.json.JsonObject;
import pl.hellopoland.user.User;

public class FacebookAPIConnector {
  private static final String FACEBOOK_API_HOST =
      "https://graph.facebook.com/me?fields=email,name,picture.type(large),location&access_token=";

  public User getUser(String token) {
    try {
      JsonObject me = me(token);
      User user = new User();
      user.setEmail(me.getString("email"));
      user.setName(me.getString("name"));
      user.setPicture(me.getJsonObject("picture").getJsonObject("data").getString("url"));
      JsonObject jsonLocation = me.getJsonObject("location");
      if (jsonLocation != null) {
        user.setLocation(jsonLocation.getString("name"));
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

}
