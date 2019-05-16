package pl.hellopoland.util;

import java.lang.System.Logger.Level;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import pl.hellopoland.rest.JsonbConfig;

public class FacebookFanPagePostReader {
  private static final System.Logger logger =
      System.getLogger(FacebookFanPagePostReader.class.getName());
  private static final Properties PROPERTIES = System.getProperties();
  private static final String FANPAGE_ID = PROPERTIES.getProperty("FB.fanpage.id");
  private static final String PAGE_ACCESS_TOKEN = PROPERTIES.getProperty("FB.page.access.token");
  private static final String QUERY_PARAM_FIELDS =
      PROPERTIES.getProperty("FB.posts.fields.query.param");
  private static final int LIMIT = 100;
  private static final String GRAPH =
      "https://graph.facebook.com/" + FANPAGE_ID + "/published_posts?access_token="
          + PAGE_ACCESS_TOKEN + "&fields=" + QUERY_PARAM_FIELDS + "&limit=" + LIMIT;
  private static LocalDateTime timer;
  private JsonObject posts;

  private FacebookFanPagePostReader() {}

  private static class SingletonHelper {
    private static final FacebookFanPagePostReader INSTANCE = new FacebookFanPagePostReader();
  }

  public static FacebookFanPagePostReader getInstance() {
    return SingletonHelper.INSTANCE;
  }

  public JsonObject readPosts() {
    downloadIfNeeded();

    // filtrowanie niechcianego posta o id=316534255768958_452515972170785
    var dataBuilder = Json.createArrayBuilder();
    posts.getJsonArray("data").forEach(p -> {
      if (!"316534255768958_452515972170785".equals(p.asJsonObject().getString("id"))) {
        dataBuilder.add(p);
      }
    });

    return Json.createObjectBuilder().add("data", dataBuilder.build())
        .add("paging", posts.get("paging")).build();
  }

  private void downloadIfNeeded() {
    var now = LocalDateTime.now(Clock.tickMinutes(ZoneId.systemDefault()));
    if (timer == null || timer.until(now, ChronoUnit.MINUTES) > 5) {
      timer = now;
      try {
        var graphURL = new URL(GRAPH);
        HttpURLConnection myWebClient = (HttpURLConnection) graphURL.openConnection();
        String responseMessage = myWebClient.getResponseMessage();
        if (myWebClient.getResponseCode() != HttpURLConnection.HTTP_OK) {
          logger.log(Level.WARNING, "Failed to read posts from funpage id = " + FANPAGE_ID + ". "
              + myWebClient.getResponseCode() + " " + responseMessage);
        } else {
          var is = myWebClient.getInputStream();
          var resp = JsonbConfig.getInstance().fromJson(is, JsonStructure.class);
          is.close();
          posts = (JsonObject) resp;
        }
        myWebClient.disconnect();
      } catch (Exception e) {
        logger.log(Level.WARNING, "Failed to read posts from funpage id = " + FANPAGE_ID, e);
      }
    }
  }
}
